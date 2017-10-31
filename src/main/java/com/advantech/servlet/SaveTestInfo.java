/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.webservice.WebServiceTX;
import com.advantech.service.TestService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class SaveTestInfo {

    private static final Logger log = LoggerFactory.getLogger(SaveTestInfo.class);
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private ParamChecker pChecker;
    
    private final String login = "LOGIN", logout = "LOGOUT", success = "success", changeDeck = "CHANGE_DECK", fail = "fail";
    private final String webservice_not_connected_message = "WebService connection  timeout, please try again.";

    @RequestMapping(value = "/SaveTestInfo", method = {RequestMethod.POST})
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
                        try {
                            String i = testService.checkDeskIsAvailable(tableNum, jobnumber);
                            if (i == null) {
                                WebServiceTX.getInstance().kanbanUserLogin(jobnumber);
                                result = testService.addTestPeople(tableNum, jobnumber) ? success : fail;
                            } else {
                                result = i;
                            }
                        } catch (Exception ex) {
                            log.error(ex.toString());
                            result = webservice_not_connected_message;
                        }
                        break;
                    case logout:
                        try {
                            WebServiceTX.getInstance().kanbanUserLogout(jobnumber);
                            result = (testService.getTableInfo(tableNum, jobnumber) != null) ? (testService.removeTestPeople(tableNum, jobnumber) ? success : fail) : fail;
                        } catch (Exception ex) {
                            log.error(ex.toString());
                            result = webservice_not_connected_message;
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
