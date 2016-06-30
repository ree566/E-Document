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
        Cookie[] cookies = req.getCookies();

        String userNo = req.getParameter("user_number");
        String table = req.getParameter("table");

        String delUserNo = req.getParameter("remove_user_number");
        String delTableId = req.getParameter("remove_table");

        boolean b = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("table")) {
                    delTableId = cookie.getValue();
                }
                if (cookie.getName().equals("user_number")) {
                    delUserNo = cookie.getValue();
                }
            }
        }
        if (pChecker.checkInputVals(userNo, table)) {
            int tableNum = Integer.parseInt(table);
            String i = testService.checkDeskIsAvailable(tableNum, userNo);
            if (i == null) {
                boolean checkInsertStatus = testService.addTestPeople(tableNum, userNo.toUpperCase().trim());
                if (checkInsertStatus == true) {
                    res.addCookie(createAndAddContentIntoCookie("table", table));
                    res.addCookie(createAndAddContentIntoCookie("user_number", userNo));
                    out.print("success");
                } else {
                    out.print("fail");
                }
            } else {
                out.print(i);
            }
            b = true;
        }
        if (pChecker.checkInputVal(delTableId) && pChecker.checkInputVal(delUserNo)) {
            b = true;
            int tableNum = Integer.parseInt(delTableId);
            if (testService.removeTestPeople(tableNum, delUserNo.toUpperCase().trim())) {
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        cookie.setMaxAge(0);
                        res.addCookie(cookie);
                    }
                }
                out.print("success");
            } else {
                out.print("fail");
                b = false;
            }
        }
        if (!b) {
            out.print("無資料異動");
        }
    }

    private Cookie createAndAddContentIntoCookie(String cookieName, String content) {
        Cookie cookie = new Cookie(cookieName, content);
        cookie.setMaxAge(12 * 60 * 60);
        return cookie;
    }
}
