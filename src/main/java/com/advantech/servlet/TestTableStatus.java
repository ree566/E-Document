/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢測試桌狀態
 */
package com.advantech.servlet;

import com.advantech.entity.Desk;
import com.advantech.service.TestLineTypeFacade;
import com.advantech.service.TestService;
import com.google.gson.Gson;
import java.io.*;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
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
public class TestTableStatus {

    @Autowired
    private TestService testService;
    
    @Autowired
    private TestLineTypeFacade ts;

    @RequestMapping(value = "/TestTableStatus/findBySitefloor", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Desk> findBySitefloor(@RequestParam(required = true) String sitefloor) {
        return testService.getDesk(sitefloor);
    }
    
    @RequestMapping(value = "/TestTableStatus/findUserNotLogin", method = {RequestMethod.GET})
    @ResponseBody
    protected Map findUserNotLogin(){
        return ts.getPEOPLE_NOT_MATCH();
    }

    @RequestMapping(value = "/TestTableStatus", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.print(new Gson().toJson(testService.getAllTableInfo()));
    }
}
