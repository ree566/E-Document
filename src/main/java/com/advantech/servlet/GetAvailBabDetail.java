/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
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
@WebServlet(name = "GetAvailBabDetail", urlPatterns = {"/GetAvailBabDetail"})
public class GetAvailBabDetail extends HttpServlet {

    private BABService babService = null;

    @Override
    public void init()
            throws ServletException {
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

        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String modelName = req.getParameter("modelName");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        out.print(new JSONObject().put("data", babService.getBAB(modelName, dateFrom, dateTo)));

    }

}
