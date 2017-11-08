/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得本次工單與上次的比較
 */
package com.advantech.controller;

import com.advantech.model.BabStatus;
import com.advantech.service.BabService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LineBalancingComparisonController {

    private static final Logger log = LoggerFactory.getLogger(LineBalancingComparisonController.class);

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/GetLineBalancingComparison", method = {RequestMethod.POST})
    protected void getLineBalancingComparison(
            @RequestParam String type,
            @RequestParam("Model_name") String modelName,
            @RequestParam("lineType") String lineType,
            @RequestParam("BABid") int bab_id,
            HttpServletRequest req,
            HttpServletResponse res
    ) throws ServletException, IOException, Exception {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject responseObject = new JSONObject();

        List<Map> l;

        switch (type) {
            case "type1":
                l = babService.getLineBalanceCompare(modelName, lineType);
                break;
            case "type2":
                l = babService.getLineBalanceCompare(bab_id);
                break;
            default:
                l = new ArrayList();
                break;
        }

        if (l != null) {
            for (Map m : l) {
                BabStatus ctrl_status = m.get("ctrl_isused") == null ? null : BabStatus.CLOSED;
                BabStatus exp_status = m.get("exp_isused") == null ? null : BabStatus.CLOSED;
                int ctrl_id = parseToInt(m.get("ctrl_id"));
                int exp_id = parseToInt(m.get("exp_id"));

                Double ctrlBalance = ctrl_id == 0 ? 0 : babService.getAvgType2(ctrl_id, ctrl_status);
                Double expBalance = exp_id == 0 ? 0 : babService.getAvgType2(exp_id, exp_status);

                responseObject.put("ctrlAvgs", ctrlBalance).put("expAvgs", expBalance);

            }
        }

        responseObject.put("data", l);
        out.print(responseObject);
    }

    private int parseToInt(Object o) {
        return o == null ? 0 : (int) o;
    }
}
