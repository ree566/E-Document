/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.TestService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "SaveTestInfo", urlPatterns = {"/SaveTestInfo"})
public class SaveTestInfo extends HttpServlet {

    private TestService testService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        testService = BasicService.getTestService();
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

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        String action = req.getParameter("action");
        String userNo = req.getParameter("userNo");
        String tableNo = req.getParameter("tableNo");
        String result;

        if (pChecker.checkInputVals(action)) { 
            if (pChecker.checkInputVals(userNo, tableNo)) {
                int tableNum = Integer.parseInt(tableNo);
                switch (action) {
                    case "LOGIN":
                        String i = testService.checkDeskIsAvailable(tableNum, userNo);
                        if (i == null) {
                            result = testService.addTestPeople(tableNum, userNo) ? "success" : "fail";
                        } else {
                            result = i;
                        }
                        break;
                    case "LOGOUT":
                        result = testService.removeTestPeople(tableNum, userNo) ? "success" : "fail";
                        break;
                    default:
                        result = "Not support action.";
                }
            } else {
                result = "Invaild input value.";
            }
        } else {
            result = "Not support action.";
        }
        out.print(result);
    }
}
