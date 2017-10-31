/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * For 前端的auto complete textbox
 */
package com.advantech.servlet;

import com.advantech.service.BABService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class GetAvailableModelName {

    @Autowired
    private BABService babService;

    @RequestMapping(value = "/GetAvailableModelName", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        List l = babService.getAvailableModelName();
        out.print(new JSONArray(l));
    }
}
