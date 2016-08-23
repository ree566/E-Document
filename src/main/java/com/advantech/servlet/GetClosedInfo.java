/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.TestService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetClosedInfo", urlPatterns = {"/GetClosedInfo"})
public class GetClosedInfo extends HttpServlet {

    private TestService testService = null;
    private BABService babService = null;

    @Override
    public void init() throws ServletException {
        testService = BasicService.getTestService();
        babService = BasicService.getBabService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String action = req.getParameter("action");

        List l;

        switch (action) {
            case "getTest":
                l = testService.getRecordTestLineType(startDate, endDate);
                break;
            case "getBab":
                l = babService.getClosedBABInfo(startDate, endDate);
                break;
            default:
                l = new ArrayList();
        }

        out.print(new JSONObject().put("data", l));
    }
}
