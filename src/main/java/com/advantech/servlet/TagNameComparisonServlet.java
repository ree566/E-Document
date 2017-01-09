/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.entity.TagNameComparison;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.TagNameComparisonService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TagNameComparisonServlet", urlPatterns = {"/TagNameComparisonServlet"})
public class TagNameComparisonServlet extends HttpServlet {

    private TagNameComparisonService tcService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        tcService = BasicService.getTagNameComparisonService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.print(new JSONObject().put("data", tcService.getAll()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String orginTagName = req.getParameter("orginTagName");
        String lampSysTagName = req.getParameter("lampSysTagName");
        String lineId = req.getParameter("lineId");
        String stationId = req.getParameter("stationId");

        if (pChecker.checkInputVals(orginTagName, lampSysTagName)) {
            List l = new ArrayList();
            Integer line = pChecker.checkInputVal(lineId) == true ? Integer.parseInt(lineId) : null;
            Integer station = pChecker.checkInputVal(stationId) == true ? Integer.parseInt(stationId) : null;

            l.add(new TagNameComparison(orginTagName, lampSysTagName, line, station));

            out.print(new JSONObject().put("Result", tcService.update(l) ? "OK" : "ERROR"));

        }

    }
}
