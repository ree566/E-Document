/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "CountermeasureServlet", urlPatterns = {"/CountermeasureServlet"})
public class CountermeasureServlet extends HttpServlet {

    private CountermeasureService cService = null;

    private static final Logger log = LoggerFactory.getLogger(CountermeasureServlet.class);

    @Override
    public void init()
            throws ServletException {
        cService = BasicService.getCountermeasureService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String action = req.getParameter("action");
        String BABid = req.getParameter("BABid");
        String reason = req.getParameter("reason");
        String errorCode_id = req.getParameter("errorCode_id");
        String solution = req.getParameter("solution");
        String editor = req.getParameter("editor");

        if (!new ParamChecker().checkInputVals(BABid)) {
            out.print(new JSONObject().put("data", "No data effect"));
            return;
        }

        int id = Integer.parseInt(BABid);

        switch (action) {
            case "select":
                out.print(new JSONObject().put("data", cService.getCountermeasure(id)));
                break;
            case "insert":
                out.print(new JSONObject().put("data", cService.insertCountermeasure(id, Integer.parseInt(errorCode_id), reason, solution, editor)));
                break;
            case "update":
                out.print(new JSONObject().put("data", cService.updateCountermeasure(id, Integer.parseInt(errorCode_id), reason, solution, editor)));
                break;
            case "delete":
                out.print(new JSONObject().put("data", cService.deleteCountermeasure(id)));
                break;
            default:
                out.print(new JSONObject().put("data", "No data effect"));
                break;
        }

    }

}
