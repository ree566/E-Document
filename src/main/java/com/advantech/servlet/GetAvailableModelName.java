/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 修改LineBalancing_Main資料表相關資訊用，"暫時"無使用(可刪除)
 */
package com.advantech.servlet;

import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetAvailableModelName", urlPatterns = {"/GetAvailableModelName"})
public class GetAvailableModelName extends HttpServlet {

//    private static final Logger log = LoggerFactory.getLogger(GetAvailableModelName.class);
    private BABService babService = null;

    @Override
    public void init()
            throws ServletException {
        babService = BasicService.getBabService();
    }

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
        List l = babService.getAvailableModelName();
        out.print(new JSONArray(l));
    }
}
