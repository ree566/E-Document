/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.entity.FBN;
import com.advantech.quartzJob.LineBalancePeopleGenerator;
import com.advantech.service.BasicService;
import com.advantech.service.FBNService;
import com.google.gson.Gson;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.println(new Gson().toJson(BasicService.getCountermeasureService().getErrorCode()));
    }

    private String getSensorTime(FBN f) {
        return convertFullDateTime(f.getLogDate(), f.getLogTime());
    }

    private String convertFullDateTime(String... str) {
        String dateString = "";
        for (String st : str) {
            dateString += st.trim() + " ";
        }
        return dateString;
    }

    private DateTime convert(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yy/MM/dd HH:mm:ss ");
        return dtf.parseDateTime(date);
    }
}
