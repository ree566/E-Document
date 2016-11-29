/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.ParamChecker;
import com.advantech.helper.StringParser;
import com.advantech.quartzJob.CellStation;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "CellScheduleJobServlet", urlPatterns = {"/CellScheduleJobServlet"})
public class CellScheduleJobServlet extends HttpServlet {

    private String jobName = null;
    private final String jobGroup = "cell";
    private final String cronTrig = "0 0/1 8-20 ? * MON-SAT *";
    private CronTrigMod ctm = null;
    private ParamChecker pc = null;

    @Override
    public void init() throws ServletException {
        ctm = CronTrigMod.getInstance();
        pc = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        try {
            String PO = req.getParameter("PO");
            String lineId = req.getParameter("lineId");

            if (pc.checkInputVals(PO, lineId)) {
                jobName = PO;
                Map data = new HashMap();
                data.put("PO", PO);
                data.put("LineId", StringParser.strToInt(lineId));

                JobKey jobKey = ctm.createJobKey(jobName, jobGroup);
                TriggerKey triggerKey = ctm.createTriggerKey(jobName, jobGroup);
                JobDetail detail = ctm.createJobDetail(jobKey, jobGroup, CellStation.class, data);
                ctm.scheduleJob(detail, triggerKey, cronTrig);
            }else{
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
