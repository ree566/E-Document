/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.entity.CellLine;
import com.advantech.helper.CronTrigMod;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ParamChecker;
import com.advantech.helper.StringParser;
import com.advantech.quartzJob.CellStation;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@WebServlet(name = "CellScheduleJobServlet", urlPatterns = {"/CellScheduleJobServlet"})
public class CellScheduleJobServlet extends HttpServlet {

    private String jobName = null;
    private final String jobGroup = "Cell";
    private final String cronTrig = "0 0/1 8-20 ? * MON-SAT *";
    private CronTrigMod ctm = null;
    private ParamChecker pc = null;
    private DatetimeGenerator dg = null;
    private Map<String, JobKey> schedJobs;

    @Override
    public void init() throws ServletException {
        ctm = CronTrigMod.getInstance();
        pc = new ParamChecker();
        dg = new DatetimeGenerator("yy-MM-dd");
        schedJobs = new HashMap();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        Object responseObject = null;

        switch (action) {
            case "insert":
                res.setContentType("text/plain");
                try {
                    String PO = req.getParameter("PO");
                    String lineId = req.getParameter("lineId");

                    if (pc.checkInputVals(PO, lineId)) {
                        CellLine cellLine = BasicService.getCellLineService().findOne(Integer.parseInt(lineId));

                        if (BasicService.getBabService().getPoTotalQuantity(PO) != null) {
                            jobName = cellLine.getName() + "_" + cellLine.getAps_lineId() + "_" + PO;
                            Map data = new HashMap();
                            data.put("PO", PO);
                            data.put("LineId", cellLine.getAps_lineId());
                            data.put("today", dg.getToday());

                            JobKey jobKey = ctm.createJobKey(jobName, jobGroup);
                            TriggerKey triggerKey = ctm.createTriggerKey(jobName, jobGroup);
                            JobDetail detail = ctm.createJobDetail(jobKey, jobGroup, CellStation.class, data);
                            ctm.scheduleJob(detail, triggerKey, cronTrig);
                            schedJobs.put(jobName, jobKey);
                            responseObject = "Job sched success.";
                        } else {
                            responseObject = "This PO is not exist, please check again.";
                        }
                    } else {
                        responseObject = "Invalid input val.";
                    }
                } catch (SchedulerException ex) {
                    responseObject = ex;
                }
                break;
            case "select":
                res.setContentType("text/plain");
                try {
                    List<JobKey> l = ctm.getJobKeys("Cell");
                    if (l.isEmpty()) {
                        responseObject = "No jobKey exist";
                    } else {
                        res.setContentType("application/json");
                        responseObject = new JSONArray(l);
                    }
                } catch (JobExecutionException ex) {
                    responseObject = ex.getCause();
                } catch (SchedulerException ex) {
                    responseObject = ex.getCause();
                }
                break;
            case "delete":
                res.setContentType("text/plain");
                String key = req.getParameter("jobKey");
                JobKey jobKey = schedJobs.get(key);
                if (jobKey == null) {
                    jobKey = ctm.createJobKey(key, this.jobGroup);
                }
                try {
                    if (ctm.isJobInScheduleExist(jobKey)) {
                        ctm.removeJob(jobKey);
                        responseObject = "Remove success";
                    } else {
                        responseObject = "Can't find this key in schedule";
                    }
                } catch (SchedulerException ex) {
                    responseObject = ex.toString();
                }
                break;
            case "truncate": {
                res.setContentType("text/plain");
                try {
                    ctm.removeJobs(this.jobGroup);
                    responseObject = "truncate success";
                } catch (SchedulerException ex) {
                    responseObject = ex.toString();
                }
                break;
            }
            default:
                res.setContentType("text/plain");
                responseObject = "Unsupport action.";
                break;
        }
        res.getWriter().print(responseObject);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
