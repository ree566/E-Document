/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Check users in the same line cannot be duplicate
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABLoginStatusServlet", urlPatterns = {"/BABLoginStatusServlet"})
public class BABLoginStatusServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BABLoginStatusServlet.class);

    private BABLoginStatusService babLoginService = null;
    private ParamChecker pChecker = null;
    private final String successMsg = "success";

    @Override
    public void init()
            throws ServletException {
        babLoginService = BasicService.getBabLoginStatusService();
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
