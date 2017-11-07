/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.entity.BabStatus;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BabService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BABTimeDetail {

    @Autowired
    private BabService babService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/BABTimeDetail", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        String isused = req.getParameter("isused");

        BabStatus status = isused == null ? null : BabStatus.CLOSED;

        out.print(new JSONObject().put("data", babService.getBABTimeDetail(Integer.parseInt(id), status)));
    }

}
