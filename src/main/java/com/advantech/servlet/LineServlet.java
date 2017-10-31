/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.LineService;
import java.io.*;
import javax.servlet.ServletException;
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
public class LineServlet {

    private static final Logger log = LoggerFactory.getLogger(LineServlet.class);

    @Autowired
    private LineService lineService;

    private final String LOGIN = "LOGIN";
    private final String LOGOUT = "LOGOUT";

    @Autowired
    ParamChecker pChecker;

    @RequestMapping(value = "/LineServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");

        PrintWriter out = res.getWriter();
        String lineNo = req.getParameter("lineNo");
        String action = req.getParameter("action");

        if (pChecker.checkInputVal(lineNo) && !lineNo.equals("-1")) {

            int line = Integer.parseInt(lineNo);
            
            String msg;
            switch (action) {
                case LOGIN:
                    msg = lineService.loginBAB(line);
                    break;
                case LOGOUT:
                    msg = lineService.logoutBAB(line);
                    break;
                default:
                    msg = "未知的動作。";
                    break;
            }
            out.print(msg);
            
        } else {
            log.error("no data filter the check");
            out.print("no data filter the check");
        }
    }
}
