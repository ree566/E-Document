/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.service.BasicService;
import com.advantech.service.TestService;
import com.google.gson.Gson;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestTableStatus", urlPatterns = {"/TestTableStatus"})
public class TestTableStatus extends HttpServlet {

    private TestService testService = null;
//    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        testService = BasicService.getTestService();
//        pChecker = new ParamChecker();
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
        out.print(new Gson().toJson(testService.getAllTableInfo()));
    }
}
