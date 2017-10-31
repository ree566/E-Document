/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Total頁面顯示(XML查詢跟目錄Total) 查詢組裝包裝、測試相關資訊後轉成XML丟給前端用
 */
package com.advantech.servlet;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import java.io.*;
import java.util.ArrayList;
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
public class GetTotal {

    @Autowired
    private TestLineTypeFacade ts;
    
    @Autowired
    private BabLineTypeFacade bs;

    @RequestMapping(value = "/GetTotal", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String type = req.getParameter("type");
        JSONObject dataObj = null;
        switch (type) {
            case "type1":
                dataObj = ts.getJSONObject();
                break;
            case "type2":
                dataObj = bs.getJSONObject();
                break;
            default:
                break;
        }
        out.print(dataObj == null ? new JSONObject().put("data", new ArrayList()) : dataObj);
    }

}
