/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.UserSelectFilter;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "AllBAB", urlPatterns = {"/AllBAB"})
public class AllBAB extends HttpServlet {

    private BABService babService = null;

    @Override
    public void init()
            throws ServletException {
        babService = BasicService.getBabService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
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
        }if("true".equals(aboveStandard)){
            usf.greaterThan("total", 10);
        }

        out.print(new JSONObject().put("data", usf.getList()));

    }
}
