/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.Line;
import com.advantech.service.LineService;
import java.io.*;
import java.util.List;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BabLineController {

    private static final Logger log = LoggerFactory.getLogger(BabLineController.class);

    @Autowired
    private LineService lineService;

    private final String LOGIN = "LOGIN";
    private final String LOGOUT = "LOGOUT";

    @RequestMapping(value = "/GetLine", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> getLine(@RequestParam(required = false) String sitefloor) {
        return sitefloor != null ? lineService.getLine(sitefloor) : lineService.getLine();
    }

    @RequestMapping(value = "/LineServlet", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam int lineNo,
            @RequestParam String action,
            HttpServletResponse res
    ) throws IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        String msg;
        switch (action) {
            case LOGIN:
                msg = lineService.loginBAB(lineNo);
                break;
            case LOGOUT:
                msg = lineService.logoutBAB(lineNo);
                break;
            default:
                msg = "未知的動作。";
                break;
        }
        out.print(msg);

    }
}
