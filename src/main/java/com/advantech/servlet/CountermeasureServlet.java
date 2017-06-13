/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 組包裝異常回覆的CRUD
 */
package com.advantech.servlet;

import com.advantech.entity.Countermeasure;
import com.advantech.helper.ParamChecker;
import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import java.util.Arrays;
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
    private ParamChecker pChecker = null;

    private static final Logger log = LoggerFactory.getLogger(CountermeasureServlet.class);

    @Override
    public void init()
            throws ServletException {
        cService = BasicService.getCountermeasureService();
        pChecker = new ParamChecker();
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
        String[] actionCodes = req.getParameterValues("actionCodes[]");
        String solution = req.getParameter("solution");
        String editor = req.getParameter("editor");
        String noDataEffectMsg = "No data effect";

        if (!pChecker.checkInputVals(BABid) && !action.equals("select") && !action.equals("getActionCode") && !action.equals("getExcel")) {
            out.print(new JSONObject().put("data", "No data effect"));
            return;
        }

//        System.out.println("action: " + action);
        int id = StringParser.strToInt(BABid);

        switch (action) {
            case "selectOne":
                Countermeasure cm = cService.getCountermeasure(id);
                if (cm == null) {
                    out.print(new JSONObject());
                } else {
                    out.print(new JSONObject(cm).put("errorCodes", cService.getErrorCode(cm.getId())).put("editors", cService.getEditor(cm.getId())));
                }
                break;
            case "select":
                out.print(new JSONObject(cService.getCountermeasure()));
                break;
            case "update":
                if (pChecker.checkArray(actionCodes)) {
                    out.print(new JSONObject().put("data", cService.updateCountermeasure(id, Arrays.asList(actionCodes), solution, editor)));
                } else {
                    out.print(new JSONObject().put("data", noDataEffectMsg));
                }
                break;
            case "delete":
                out.print(new JSONObject().put("data", cService.deleteCountermeasure(id)));
                break;
            case "getActionCode":
                out.print(new JSONObject().put("data", cService.getActionCode()));
                break;
            default:
                out.print(new JSONObject().put("data", noDataEffectMsg));
                break;
        }

    }

}
