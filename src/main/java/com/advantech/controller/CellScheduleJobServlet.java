/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.controller;

import com.advantech.model.Cell;
import com.advantech.model.CellLine;
import com.advantech.helper.CronTrigMod;
import com.advantech.quartzJob.CellStation;
import com.advantech.service.CellLineService;
import com.advantech.service.CellService;
import com.advantech.service.PassStationService;
import java.io.*;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.*;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@Controller
@RequestMapping("/CellScheduleJobServlet")
public class CellScheduleJobServlet {

    private final String jobGroup = "Cell";
    private final String cronTrig = "0 0/1 8-20 ? * MON-SAT *";

    private Map<String, JobKey> schedJobs;

    @Autowired
    private CellService cellService;

    @Autowired
    private CellLineService cellLineService;

    @Autowired
    private PassStationService passStationService;

    @Autowired
    private CronTrigMod ctm;

    @PostConstruct
    public void init() {
        schedJobs = new HashMap();
        this.initProcessingCells();
    }

    @RequestMapping(value = "/select", method = {RequestMethod.GET})
    @ResponseBody
    public List<JobKey> select() throws SchedulerException {
        return getSchedJobs();
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    public String insert(
            @RequestParam(value = "PO") String po,
            @RequestParam(value = "modelname") String modelName,
            @RequestParam int lineId,
            @RequestParam(value = "type") String type,
            HttpServletResponse res
    ) throws IOException, SchedulerException {
        CellLine cellLine = cellLineService.findOne(lineId);

        if (cellLine.isOpened()) {
            if (cellService.getCellProcessing(lineId).isEmpty()) {
                Cell cell = new Cell(lineId, type, po, modelName);
                cellService.insert(cell);
                schedNewJobs(cell);
                return "success";
            } else {
                throw new IllegalArgumentException("Some PO in this line is already processing");
            }
        } else {
            throw new IllegalArgumentException("This line is not opened, data insert fail");
        }

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(
            @RequestParam int lineId,
            @RequestParam("PO") String po,
            HttpServletResponse res
    ) throws IOException, SchedulerException {

        List<Cell> cells = cellService.getCellProcessing(lineId);
        if (!cells.isEmpty()) {
            Cell cell = (Cell) cells.get(0);
            CellLine cellLine = cellLineService.findOne(cell.getLineId());
            passStationService.checkDifferenceAndInsert(po, cell.getType(), cellLine.getAps_lineId()); //Don't forget to check the xml data and insert at last.
            cellService.delete(cell);
            removeJob(cell);
            return "success";
        } else {
            throw new IllegalArgumentException("fail");
        }

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
        return ctm.getJobKeys("Cell");
    }

    private void schedNewJobs(Cell cell) throws SchedulerException {
        String po = cell.getPO();
        int lineId = cell.getLineId();
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
    }

    private void removeJob(Cell cell) throws SchedulerException {
        int lineId = cell.getLineId();
        CellLine cellLine = cellLineService.findOne(lineId);
        if (cellLine != null) {
            String key = Integer.toString(lineId);
            JobKey jobKey = schedJobs.get(key);
            if (jobKey == null) {
                String jobKeyName = this.generateCellJobKeyName(cellLine, cell);
                jobKey = ctm.createJobKey(jobKeyName, this.jobGroup);
            }
            if (ctm.isJobInScheduleExist(jobKey)) {
                ctm.removeJob(jobKey);
            }
        }
    }

    private String generateCellJobKeyName(CellLine cellLine, Cell cell) {
        return cellLine.getName() + "_" + cell.getType() + "_" + cell.getPO();
    }
}
