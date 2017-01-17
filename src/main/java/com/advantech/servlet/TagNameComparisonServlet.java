/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.entity.Line;
import com.advantech.entity.TagNameComparison;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.TagNameComparisonService;
import java.io.*;
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
        doPost(req, res);
//        res.setContentType("application/json");
//        PrintWriter out = res.getWriter();
//        out.print(new JSONObject().put("data", tcService.getAll()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String lineId = req.getParameter("lineId");
        String startPosition = req.getParameter("startPosition");
        String people = req.getParameter("people");

        if (pChecker.checkInputVals(lineId, startPosition, people) == true) {

            Line line = BasicService.getLineService().getLine(Integer.parseInt(lineId));

            if (line == null || !line.isOpened()) {
                out.print(new JSONObject().put("status", "line is not exist or line is not opened"));
                return;
            }

            int position = Integer.parseInt(startPosition);
            int p = Integer.parseInt(people);

            if (position < 1 || position > line.getPeople() - 1 || p < 1 || p > line.getPeople()) {
                out.print(new JSONObject().put("status", "position or people out of bounds"));
                return;
            }

            List<TagNameComparison> l = tcService.getByLine(line.getId());

            if (position + p - 1 > l.size()) {
                out.print(new JSONObject().put("status", "index out of bounds"));
                return;
            }

            for (TagNameComparison tagNameComp : l) {
                Integer currentStation = tagNameComp.getDefaultStationId();
                if (currentStation == null) {
                    continue;
                }
                if (currentStation >= position) {
                    tagNameComp.setStationId(tagNameComp.getDefaultStationId()- position + 1);
                } else {
                    tagNameComp.setStationId(null);
                }
            }
            out.print(new JSONObject().put("status", tcService.update(l) ? "ok" : "fail"));

//            out.print(new JSONObject().put("status", l));
        } else {
            out.print(new JSONObject().put("status", "fail"));
        }

    }
}
