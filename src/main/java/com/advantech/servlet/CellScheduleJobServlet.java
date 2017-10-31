/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.servlet;

import com.advantech.entity.Cell;
import com.advantech.entity.CellLine;
import com.advantech.helper.CronTrigMod;
import com.advantech.helper.ParamChecker;
import com.advantech.quartzJob.CellStation;
import com.advantech.service.CellLineService;
import com.advantech.service.CellService;
import java.io.*;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@Controller
public class CellScheduleJobServlet {

    private final String jobGroup = "Cell";
    private final String cronTrig = "0 0/1 8-20 ? * MON-SAT *";
    
    @Autowired
    private ParamChecker pc;
    
    private Map<String, JobKey> schedJobs;
    
    @Autowired
    private CellService cellService;
    
    @Autowired
    private CellLineService cellLineService;
    
    @Autowired
    private CellStation cellStation;

    @PostConstruct
    public void init() {
        schedJobs = new HashMap();
        this.initProcessingCells();
    }

    @RequestMapping(value = "/CellScheduleJobServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        Object responseObject;

        switch (action) {
            case "insert":
                res.setContentType("text/plain");
                try {
                    String PO = req.getParameter("PO");
                    String modelname = req.getParameter("modelname");
                    String lineId = req.getParameter("lineId");
                    String type = req.getParameter("type");
                    if (pc.checkInputVals(PO, lineId) == true) {

                        int line = Integer.parseInt(lineId);

                        CellLine cellLine = cellLineService.findOne(line);

                        if (cellLine.isOpened()) {
                            if (cellService.getCellProcessing(line).isEmpty()) {
//                                WorkTimeService workTimeService = BasicService.getWorkTimeService();
//                                if (workTimeService.getAssyStandardTime(modelname) != null) { //Check 標工
                                Cell cell = new Cell(line, type, PO, modelname);
                                if (cellService.insert(cell)) {
                                    responseObject = this.schedNewJobs(cell) ? "success" : "fail";
                                } else {
                                    responseObject = "fail";
                                }
//                                } else {
//                                    responseObject = "PO is not exist";
//                                }
                            } else {
                                responseObject = "Some PO in this line is already processing";
                            }
                        } else {
                            responseObject = "This line is not opened, data insert fail";
                        }
                    } else {
                        responseObject = "Invalid input values";
                    }

                } catch (SchedulerException ex) {
                    responseObject = "fail";
                }
                break;
            case "select":
                res.setContentType("text/plain");
                try {
                    List l = getSchedJobs();
                    if (l.isEmpty()) {
                        responseObject = "No jobKey exist";
                    } else {
                        res.setContentType("application/json");
                        responseObject = new JSONArray(l);
                    }
                } catch (SchedulerException ex) {
                    responseObject = "fail";
                }
                break;
            case "delete":
                res.setContentType("text/plain");
                String lineId = req.getParameter("lineId");
                String PO = req.getParameter("PO");
                if (pc.checkInputVals(PO, lineId) == true) {
                    try {
                        int line = Integer.parseInt(lineId);
                        List<Cell> cells = cellService.getCellProcessing(line);
                        if (!cells.isEmpty()) {
                            Cell cell = (Cell) cells.get(0);
                            CellLine cellLine = cellLineService.findOne(cell.getLineId());
                            cellStation.checkDifferenceAndInsert(PO, cell.getType(), cellLine.getAps_lineId()); //Don't forget to check the xml data and insert at last.
                            if (cellService.delete(cell) == true) {
                                responseObject = removeJob(cell) ? "success" : "fail";
                            } else {
                                responseObject = "fail";
                            }
                        } else {
                            responseObject = "fail";
                        }
                    } catch (SchedulerException ex) {
                        responseObject = "fail";
                    }
                } else {
                    responseObject = "Invalid input values";
                }
                break;
            default:
                res.setContentType("text/plain");
                responseObject = "Unsupport action.";
                break;
        }
        res.getWriter().print(responseObject);
    }

    private void initProcessingCells() {
        List<Cell> l = cellService.getCellProcessing();
        for (Cell cell : l) {
            try {
                this.schedNewJobs(cell);
            } catch (SchedulerException ex) {
                out.println(ex);
            }
        }
    }

    private List<JobKey> getSchedJobs() throws SchedulerException {
        return CronTrigMod.getInstance().getJobKeys("Cell");
    }

    private boolean schedNewJobs(Cell cell) throws SchedulerException {
        String po = cell.getPO();
        int lineId = cell.getLineId();
        CronTrigMod ctm = CronTrigMod.getInstance();
        CellLine cellLine = cellLineService.findOne(lineId);
        String jobKeyName = this.generateCellJobKeyName(cellLine, cell);
        Map data = new HashMap();
        data.put("PO", po);
        data.put("type", cell.getType());
        data.put("apsLineId", cellLine.getAps_lineId());
        JobKey jobKey = ctm.createJobKey(jobKeyName, jobGroup);
        TriggerKey triggerKey = ctm.createTriggerKey(jobKeyName, jobGroup);
        JobDetail detail = ctm.createJobDetail(jobKey, jobGroup, CellStation.class, data);
        ctm.scheduleJob(detail, triggerKey, cronTrig);
        schedJobs.put(Integer.toString(cellLine.getId()), jobKey);
        return true;
    }

    private boolean removeJob(Cell cell) throws SchedulerException {
        int lineId = cell.getLineId();
        CronTrigMod ctm = CronTrigMod.getInstance();
        CellLine cellLine = cellLineService.findOne(lineId);
        if (cellLine == null) {
            return false;
        }
        String key = Integer.toString(lineId);
        JobKey jobKey = schedJobs.get(key);
        if (jobKey == null) {
            String jobKeyName = this.generateCellJobKeyName(cellLine, cell);
            jobKey = ctm.createJobKey(jobKeyName, this.jobGroup);
        }
        if (ctm.isJobInScheduleExist(jobKey)) {
            ctm.removeJob(jobKey);
            return true;
        } else {
            return false;
        }
    }

    private String generateCellJobKeyName(CellLine cellLine, Cell cell) {
        return cellLine.getName() + "_" + cell.getType() + "_" + cell.getPO();
    }
}
