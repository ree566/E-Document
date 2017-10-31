/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 起始站別設定
 */
package com.advantech.servlet;

import com.advantech.entity.Line;
import com.advantech.entity.TagNameComparison;
import com.advantech.helper.ParamChecker;
import com.advantech.service.LineService;
import com.advantech.service.TagNameComparisonService;
import java.io.*;
import java.util.List;
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
public class TagNameComparisonServlet {
    
    @Autowired
    private LineService lineService;

    @Autowired
    private TagNameComparisonService tcService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/TagNameComparisonServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String lineId = req.getParameter("lineId");
        String startPosition = req.getParameter("startPosition");
        String people = req.getParameter("people");

        if (pChecker.checkInputVals(lineId, startPosition, people) == true) {

            Line line = lineService.getLine(Integer.parseInt(lineId));

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

        } else {
            out.print(new JSONObject().put("status", "fail"));
        }

    }
}
