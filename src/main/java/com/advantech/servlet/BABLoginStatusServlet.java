/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Check users in the same line cannot be duplicate
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
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
public class BABLoginStatusServlet {

    private static final Logger log = LoggerFactory.getLogger(BABLoginStatusServlet.class);

    @Autowired
    private BABLoginStatusService babLoginService;
    
    @Autowired
    private ParamChecker pChecker;
    
    private final String successMsg = "success";

    @RequestMapping(value = "/BABLoginStatusServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String lineNo = req.getParameter("lineNo");
        String jobnumber = req.getParameter("jobnumber");
        String station = req.getParameter("station");

        String action = req.getParameter("action");

        if (pChecker.checkInputVals(action, lineNo, jobnumber, station)) {
            switch (action) {
                case "LOGIN":
                    babLoginService.getBABLoginStatus();
                    break;
                case "LOGOUT":
                    break;
                case "CHANGE_USER":
                    break;
            }
        }
    }
}
