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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetTotal", urlPatterns = {"/GetTotal"})
public class GetTotal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
//        res.sendError(HttpServletResponse.SC_FORBIDDEN);
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String type = req.getParameter("type");
        JSONObject dataObj = null;
        switch (type) {
            case "type1":
//                dataObj = DataTransformer.getTestJsonObj();
                dataObj = TestLineTypeFacade.getInstance().getJSONObject();
                break;
            case "type2":
//                dataObj = DataTransformer.getBabJsonObj();
                dataObj = BabLineTypeFacade.getInstance().getJSONObject();
                break;
            default:
                break;
        }
        out.print(dataObj == null ? new JSONObject().put("data", new ArrayList()) : dataObj);
    }

}
