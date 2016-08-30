/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.entity.FBN;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
import com.advantech.service.BasicService;
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
        PrintWriter out = res.getWriter();
        String babid = req.getParameter("babid");
        String station = req.getParameter("station");
        String jobnumber = req.getParameter("jobnumber");
        String action = req.getParameter("action");
        if (new ParamChecker().checkInputVals(babid, station, jobnumber, action)) {
            BABLoginStatusService bs = BasicService.getBabLoginStatusService();
            int babId = Integer.parseInt(babid);
            int stat = Integer.parseInt(station);

            switch (action) {
                case "insert":
                    out.print(bs.babLogin(babId, stat, jobnumber));
                    break;
                case "update":
                    out.print(bs.changeUser(babId, stat, jobnumber));
                    break;
                case "delete":
                    out.print(bs.deleteUserFromStation(babId, stat));
                    break;
                case "select":
                    out.print(bs.getBABLoginStatus());
                    break;
                default:
                    out.print("Invaild action.");
            }
        } else {
            out.print("Invaild input value.");
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
