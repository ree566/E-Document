/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得該工單每pcs的線平衡
 */
package com.advantech.servlet;

import com.advantech.entity.BABStatus;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABService;
import java.io.*;
import java.util.ArrayList;
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
public class LineBalanceDetail {

    @Autowired
    private BABService babService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/LineBalanceDetail", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        String isused = req.getParameter("isused");
        List balnPerGroup;
        if (pChecker.checkInputVals(id)) {
            int i = Integer.parseInt(id);
            BABStatus status = isused == null ? null : BABStatus.CLOSED;
            balnPerGroup = babService.getLineBalanceDetail(i, status);
        } else {
            balnPerGroup = new ArrayList();
        }
        out.print(new JSONObject().put("data", balnPerGroup));
    }
}
