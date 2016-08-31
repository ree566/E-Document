/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試資料用servlet 無作用(可刪除)
 */
package com.advantech.servlet;

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
            Double d = babService.getTotalAvg(id);
            obj.put("data", babService.getSensorDiffChart(id, isNull(isused, 0)));
            obj.put("avg", d.intValue());
        }
        out.print(obj);
    }

    private int isNull(String o, int i) {
        return o == null ? i : Integer.parseInt(o);
    }

}
