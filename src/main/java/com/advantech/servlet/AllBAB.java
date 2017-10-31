/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.UserSelectFilter;
import com.advantech.service.BABService;
import java.io.*;
import java.util.List;
import java.util.Map;
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
public class AllBAB {

    @Autowired
    private BABService babService;

    @RequestMapping(value = "/AllBAB", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String lineType = req.getParameter("lineType");
        String sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String aboveStandard = req.getParameter("aboveStandard");

        List<Map> l = babService.getBABInfo(startDate, endDate);
        UserSelectFilter usf = new UserSelectFilter();
        usf.setList(l);
        if (!"-1".equals(lineType)) {
            usf.filterData("lineType", lineType);
        }
        if (!"-1".equals(sitefloor)) {
            usf.filterData("sitefloor", Integer.parseInt(sitefloor));
        }
        if ("true".equals(aboveStandard)) {
            usf.greaterThan("total", 10 - 1);
        }

        out.print(new JSONObject().put("data", usf.getList()));

    }
}
