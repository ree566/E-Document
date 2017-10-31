/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得可使用的組裝線別
 */
package com.advantech.servlet;

import com.advantech.entity.Line;
import com.advantech.service.LineService;
import com.google.gson.Gson;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONArray;
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
public class GetLine {
    
    @Autowired
    private LineService lineService;

    @RequestMapping(value = "/GetLine", method = {RequestMethod.GET})
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<Line> l = lineService.getLine();
        JSONArray arr = new JSONArray();
        for (Line line : l) {
            arr.put(new JSONObject().put(Integer.toString(line.getId()), line.getName()));
        }

        out.println(arr);
    }

    @RequestMapping(value = "/GetLine", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String sitefloor = req.getParameter("sitefloor");

        out.print(new Gson().toJson(sitefloor == null ? lineService.getLine() : lineService.getLine(sitefloor)));

    }
}
