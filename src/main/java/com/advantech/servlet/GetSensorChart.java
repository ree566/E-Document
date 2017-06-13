/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 將計算紀錄回饋給前端，做成chart
 */
package com.advantech.servlet;

import com.advantech.entity.BABStatus;
import com.advantech.helper.ParamChecker;
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
@WebServlet(name = "GetSensorChart", urlPatterns = {"/GetSensorChart"})
public class GetSensorChart extends HttpServlet {

    private BABService babService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        babService = BasicService.getBabService();
        pChecker = new ParamChecker();
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
        String babid = req.getParameter("id");
        String isused = req.getParameter("isused");
        JSONObject obj = new JSONObject();
        if (pChecker.checkInputVals(babid)) {
            int id = Integer.parseInt(babid);
            BABStatus status = isused == null ? null : BABStatus.CLOSED;
            obj.put("data", babService.getSensorDiffChart(id, status));
            obj.put("avg", babService.getTotalAvg(id).intValue());
        }
        out.print(obj);
    }

}
