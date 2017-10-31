/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢Cell桌狀態
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.CellService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
public class CellSearch {

    @Autowired
    private CellService cellService;

    @Autowired
    private ParamChecker pc;

    @RequestMapping(value = "/CellSearch", method = {RequestMethod.POST})
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
