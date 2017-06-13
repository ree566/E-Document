/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢Cell桌狀態
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.CellService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "CellSearch", urlPatterns = {"/CellSearch"})
public class CellSearch extends HttpServlet {

    private CellService cellService = null;
    private ParamChecker pc = null;

    @Override
    public void init()
            throws ServletException {
        cellService = BasicService.getCellService();
        pc = new ParamChecker();
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
        if (pc.checkInputVal(action)) {
            switch (action) {
                case "getHistory":
                    String startDate = req.getParameter("startDate");
                    String endDate = req.getParameter("endDate");
                    List l = null;
                    if (pc.checkInputVals(startDate, endDate)) {
                        l = cellService.cellHistoryView(startDate, endDate);
                    } else {
                        l = cellService.cellHistoryView();
                    }
                    out.print(new JSONObject().put("data", l));
                    break;
                case "getProcessing":
                    String lineId = req.getParameter("lineId");
                    if (pc.checkInputVal(lineId) == true) {
                        out.print(new JSONObject().put("data", cellService.getCellProcessing(Integer.parseInt(lineId))));
                    } else {
                        out.print(new JSONObject().put("data", cellService.getCellProcessing()));
                    }
                    break;
                default:
                    out.print("Unsupport action");
                    break;
            }
        } else {
            out.print("Invalid input val");
        }
    }

}
