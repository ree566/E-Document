/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得測試線別效率紀錄
 */
package com.advantech.servlet;

import com.advantech.service.TestService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
public class GetClosedInfo {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/GetClosedInfo", method = {RequestMethod.POST})
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
            default:
                l = new ArrayList();
        }

        out.print(new JSONObject().put("data", l));
    }
}
