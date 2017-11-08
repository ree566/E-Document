/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 將計算紀錄回饋給前端，做成chart
 */
package com.advantech.controller;

import com.advantech.model.BabStatus;
import com.advantech.service.BabService;
import java.io.*;
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
public class GetSensorChart {

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/GetSensorChart", method = {RequestMethod.POST})
    protected void getSensorChart(
            @RequestParam("id") int bab_id,
            @RequestParam(required = false) Integer isused,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        JSONObject obj = new JSONObject();

        BabStatus status = isused == null ? null : BabStatus.CLOSED;
        obj.put("data", babService.getSensorDiffChart(bab_id, status));
        obj.put("avg", babService.getTotalAvg(bab_id).intValue());

        out.print(obj);
    }

}
