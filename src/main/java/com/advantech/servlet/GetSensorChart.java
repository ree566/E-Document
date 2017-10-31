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
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class GetSensorChart {

    @Autowired
    private BABService babService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/GetSensorChart", method = {RequestMethod.POST})
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
