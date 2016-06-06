/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.LineService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "LineLogin", urlPatterns = {"/LineLogin"})
public class LineLogin extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LineLogin.class);

    private LineService lineService = null;
    ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        lineService = BasicService.getLineService();
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

        res.setCharacterEncoding("utf-8");
        res.setContentType("text/plain");

        PrintWriter out = res.getWriter();
        String lineNo = req.getParameter("lineno");
        String flag = req.getParameter("Flag");

        if (pChecker.checkInputVal(lineNo) && !lineNo.equals("-1")) {
            try {

                int line = Integer.parseInt(lineNo);
                JSONObject lineState = lineService.getLineState(line);

                String msg;
                if (flag.equals("TRUE")) {
                    msg = lineService.loginBAB(lineState, line);
                    if ("success".equals(msg)) {
                        lineState.put("linestate", msg);
                        String content = lineState.toString();
                        Cookie c = createAndAddContentIntoCookie(content);
                        res.addCookie(c);
                    }
                } else {
                    msg = lineService.logoutBAB(lineState, line);
                }
                out.print(msg);
            } catch (JSONException ex) {
                log.error(ex.toString());
            }
        }
    }

    private Cookie createAndAddContentIntoCookie(String content) {
        Cookie cookie = new Cookie("servermsg", content);
        cookie.setMaxAge(12 * 60 * 60);
        return cookie;
    }

}
