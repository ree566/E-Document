/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得該工單每pcs的線平衡
 */
package com.advantech.controller;

import com.advantech.model.BabStatus;
import com.advantech.service.BabService;
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
public class LineBalanceDetail {

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/LineBalanceDetail", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam int id,
            @RequestParam Integer isused,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        BabStatus status = isused == null ? null : BabStatus.CLOSED;
        List balnPerGroup = babService.getLineBalanceDetail(id, status);

        out.print(new JSONObject().put("data", balnPerGroup));
    }
}
