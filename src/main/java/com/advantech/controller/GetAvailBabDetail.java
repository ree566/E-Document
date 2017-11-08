/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得時間內的工單(babTotal.jsp用)
 */
package com.advantech.controller;

import com.advantech.service.BabService;
import java.io.*;
import javax.servlet.ServletException;
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
public class GetAvailBabDetail {

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/GetAvailBabDetail", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String modelName = req.getParameter("modelName");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        out.print(new JSONObject().put("data", babService.getBAB(modelName, dateFrom, dateTo)));

    }

}
