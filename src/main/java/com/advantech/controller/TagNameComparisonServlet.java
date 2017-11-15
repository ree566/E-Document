/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 起始站別設定
 */
package com.advantech.controller;

import com.advantech.model.Line;
import com.advantech.model.LineStatus;
import com.advantech.model.TagNameComparison;
import com.advantech.service.LineService;
import com.advantech.service.TagNameComparisonService;
import java.io.*;
import java.util.List;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/TagNameComparisonServlet", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam("lineId") int line_id,
            @RequestParam int startPosition,
            @RequestParam int people,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        Line line = lineService.getLine(line_id);

        if (line == null || line.getLineStatus() != LineStatus.OPEN) {
            throw new IllegalArgumentException("line is not exist or line is not opened");
        }

        if (startPosition < 1 || startPosition > line.getPeople() - 1 || people < 1 || people > line.getPeople()) {
            throw new IllegalArgumentException("position or people out of bounds");
        }

        List<TagNameComparison> l = tcService.getByLine(line.getId());

        if (startPosition + people - 1 > l.size()) {
            throw new IllegalArgumentException("index out of bounds");
        }

        for (TagNameComparison tagNameComp : l) {
            Integer currentStation = tagNameComp.getDefaultStationId();
            if (currentStation == null) {
                continue;
            }
            if (currentStation >= startPosition) {
                tagNameComp.setStationId(tagNameComp.getDefaultStationId() - startPosition + 1);
            } else {
                tagNameComp.setStationId(null);
            }
        }
        out.print(new JSONObject().put("status", tcService.update(l) ? "ok" : "fail"));

    }
}
