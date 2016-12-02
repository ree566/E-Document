/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.helper.CronTrigMod;
import com.advantech.model.BasicDAO;
import com.advantech.quartzJob.ClearPollingJob;
import com.advantech.quartzJob.CountermeasureAlarm;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        try {
            List<JobKey> l = CronTrigMod.getInstance().getJobKeys("Cell");
            if (l.isEmpty()) {
                out.println("No jobKey exist");
            } else {
                for (JobKey j : l) {
                    out.println(j);
                }
            }
//            new ClearPollingJob().execute(null);
        } catch (JobExecutionException ex) {
            out.println(ex.toString());
        } catch (SchedulerException ex) {
            out.println(ex.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        out.println(BasicService.getBabService().getAvg(Integer.parseInt(id)));
    }
}
