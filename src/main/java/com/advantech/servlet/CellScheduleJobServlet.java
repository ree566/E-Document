/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ParamChecker;
import com.advantech.helper.StringParser;
import com.advantech.quartzJob.CellStation;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.joda.time.format.DateTimeParser;
import org.quartz.JobDetail;
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

    @Override
    public void init() throws ServletException {
        ctm = CronTrigMod.getInstance();
        pc = new ParamChecker();
        dg = new DatetimeGenerator("yy-MM-dd");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        //Update the jobKey to lineId
        //combine LineId and PO to jobKey
        //重複投入工單問題
        try {
            String PO = req.getParameter("PO");
            String lineId = req.getParameter("lineId");

            if (pc.checkInputVals(PO, lineId)) {

                if (BasicService.getBabService().getPoTotalQuantity(PO) != null) {
                    jobName = lineId + "_" + PO;
                    Map data = new HashMap();
                    data.put("PO", PO);
                    data.put("LineId", StringParser.strToInt(lineId));
                    data.put("today", dg.getToday());

                    JobKey jobKey = ctm.createJobKey(jobName, jobGroup);
                    TriggerKey triggerKey = ctm.createTriggerKey(jobName, jobGroup);
                    JobDetail detail = ctm.createJobDetail(jobKey, jobGroup, CellStation.class, data);
                    ctm.scheduleJob(detail, triggerKey, cronTrig);
                    out.println("Job sched success.");
                } else {
                    out.println("This PO is not exist, please check again.");
                }
            } else {
                out.println("Invalid input val.");
            }
        } catch (SchedulerException ex) {
            out.println(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
