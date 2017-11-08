/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.UserSelectFilter;
import com.advantech.model.BabStatus;
import com.advantech.service.BabService;
import com.advantech.webservice.WebServiceRV;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
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
public class BabController {

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/AllBAB", method = {RequestMethod.POST})
    protected void findAll(
            @RequestParam String lineType,
            @RequestParam String sitefloor,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam boolean aboveStandard,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<Map> l = babService.getBABInfo(startDate, endDate);
        UserSelectFilter usf = new UserSelectFilter();
        usf.setList(l);
        if (!"-1".equals(lineType)) {
            usf.filterData("lineType", lineType);
        }
        if (!"-1".equals(sitefloor)) {
            usf.filterData("sitefloor", Integer.parseInt(sitefloor));
        }
        if (aboveStandard) {
            usf.greaterThan("total", 10 - 1);
        }
        out.print(new JSONObject().put("data", usf.getList()));
    }

    @RequestMapping(value = "/BabSearch", method = {RequestMethod.POST})
    protected void babSearch(
            @RequestParam(required = false) String po,
            @RequestParam(required = false, value = "saveline") String saveLine,
            @RequestParam(required = false, value = "po_getBAB") String poGetBab,
            @RequestParam(required = false, value = "po_saveline") String poSaveLine,
            HttpServletResponse res
    ) throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        if (po != null) {
            try {
                String modelname = WebServiceRV.getInstance().getModelnameByPo(po);
                out.print(modelname == null ? "data not found" : convertString(modelname));
            } catch (Exception e) {
                out.print("data not found");
            }
        } else if (saveLine != null) {
            out.print(new Gson().toJson(babService.getProcessingBABByLine(Integer.parseInt(saveLine))));
        } else if (poGetBab != null && poSaveLine != null) {
            out.print(babService.getBABInfoWithSensorState(poGetBab, poSaveLine));
        }
    }

    private String convertString(String input) {
        String converstring = "";
        Pattern p = Pattern.compile("[\\w|-]");
        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            converstring += matcher.group();
        }
        return converstring;
    }

    @RequestMapping(value = "/BABTimeDetail", method = {RequestMethod.POST})
    protected void babTimeDetail(
            @RequestParam int id,
            @RequestParam Integer isused,
            HttpServletResponse res
    ) throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        BabStatus status = isused == null ? null : BabStatus.CLOSED;

        out.print(new JSONObject().put("data", babService.getBABTimeDetail(id, status)));
    }

}
