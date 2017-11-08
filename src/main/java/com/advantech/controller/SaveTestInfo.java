/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.controller;

import com.advantech.webservice.WebServiceTX;
import com.advantech.service.TestService;
import java.io.*;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class SaveTestInfo {

    private static final Logger log = LoggerFactory.getLogger(SaveTestInfo.class);

    @Autowired
    private TestService testService;

    private final String login = "LOGIN", logout = "LOGOUT", success = "success", changeDeck = "CHANGE_DECK", fail = "fail";
    private final String webservice_not_connected_message = "WebService connection  timeout, please try again.";

    @RequestMapping(value = "/SaveTestInfo", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam String action,
            @RequestParam("userNo") String jobnumber,
            @RequestParam("tableNo") int tableNo,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        String result;

        switch (action) {
            case login:
                try {
                    String i = testService.checkDeskIsAvailable(tableNo, jobnumber);
                    if (i == null) {
                        WebServiceTX.getInstance().kanbanUserLogin(jobnumber);
                        result = testService.addTestPeople(tableNo, jobnumber) ? success : fail;
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
                    result = (testService.getTableInfo(tableNo, jobnumber) != null) ? (testService.removeTestPeople(tableNo, jobnumber) ? success : fail) : fail;
                } catch (Exception ex) {
                    log.error(ex.toString());
                    result = webservice_not_connected_message;
                }
                break;
            case changeDeck:
                result = (testService.getTableInfo(tableNo, jobnumber) != null) ? (testService.removeTestPeople(tableNo, jobnumber) ? success : fail) : fail;
                break;
            default:
                result = "Not support action.";
        }

        out.print(result);
    }
}
