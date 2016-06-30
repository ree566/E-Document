/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetCloseBABInfo", urlPatterns = {"/GetCloseBABInfo"})
public class GetCloseBABInfo extends HttpServlet {

    private BABService babService = null;

    @Override
    public void init() throws ServletException {
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

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        out.print(new JSONObject().put("data", babService.getClosedBABInfo(startDate, endDate)));
    }
}
