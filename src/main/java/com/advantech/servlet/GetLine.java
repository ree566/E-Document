/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.entity.Line;
import com.advantech.service.BasicService;
import com.advantech.service.LineService;
import com.google.gson.Gson;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetLine", urlPatterns = {"/GetLine"})
public class GetLine extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<Line> l = BasicService.getLineService().getLine();
        JSONArray arr = new JSONArray();
        for (Line line : l) {
            arr.put(new JSONObject().put(Integer.toString(line.getId()), line.getName()));
        }

        out.println(arr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String sitefloor = req.getParameter("sitefloor");

        LineService lineService = BasicService.getLineService();

        out.print(new Gson().toJson(sitefloor == null ? lineService.getLine() : lineService.getLine(sitefloor)));

    }
}
