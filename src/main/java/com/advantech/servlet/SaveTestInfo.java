/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.webservice.WebServiceTX;
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
    private final String login = "LOGIN", logout = "LOGOUT", success = "success", changeDeck = "CHANGE_DECK", fail = "fail";

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
        String jobnumber = req.getParameter("userNo");
        String tableNo = req.getParameter("tableNo");
        String result;

        if (pChecker.checkInputVals(action)) {
            if (pChecker.checkInputVals(jobnumber, tableNo)) {
                int tableNum = Integer.parseInt(tableNo);
                switch (action) {
                    case login:
                        String i = testService.checkDeskIsAvailable(tableNum, jobnumber);
                        if (i == null) {
                            result = testService.addTestPeople(tableNum, jobnumber) ? success : fail;
                            if (result.equals(success)) {
                                WebServiceTX.getInstance().kanbanUserLogin(jobnumber);
                            }
                        } else {
                            result = i;
                        }
                        break;
                    case logout:
                        result = (testService.getTableInfo(tableNum, jobnumber) != null) ? (testService.removeTestPeople(tableNum, jobnumber) ? success : fail) : fail;
                        if (result.equals(success)) {
                            WebServiceTX.getInstance().kanbanUserLogout(jobnumber);
                        }
                        break;
                    case changeDeck:
                        result = (testService.getTableInfo(tableNum, jobnumber) != null) ? (testService.removeTestPeople(tableNum, jobnumber) ? success : fail) : fail;
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
