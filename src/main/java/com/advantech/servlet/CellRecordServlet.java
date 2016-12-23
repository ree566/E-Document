/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.PassStationService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@WebServlet(name = "CellRecordServlet", urlPatterns = {"/CellRecordServlet"})
public class CellRecordServlet extends HttpServlet {

    private PassStationService passStationService;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        passStationService = BasicService.getPassStationService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String PO = req.getParameter("PO");
        String lineId = req.getParameter("lineId");
        String minPcs = req.getParameter("minPcs");
        String maxPcs = req.getParameter("maxPcs");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        JSONObject jsonObject = new JSONObject();

        List l = null;
//        if (pChecker.checkInputVals(PO, lineId)) {
//            //equals false = not setting topN filter
//            if (pChecker.checkInputVal(minPcs) == false && pChecker.checkInputVal(maxPcs) == false) {
//                l = passStationService.getCellPerPcsHistory(PO, Integer.parseInt(lineId));
//            } else {
        l = passStationService.getAllCellPerPcsHistory(
                pChecker.checkInputVal(PO) ? PO : null,
                pChecker.checkInputVal(lineId) ? Integer.parseInt(lineId) : null,
                pChecker.checkInputVal(minPcs) ? Integer.parseInt(minPcs) : null,
                pChecker.checkInputVal(maxPcs) ? Integer.parseInt(maxPcs) : null,
                pChecker.checkInputVal(startDate) ? startDate : null,
                pChecker.checkInputVal(endDate) ? endDate : null
        );
//            }
//        } else {
//            String startDate = req.getParameter("startDate");
//            String endDate = req.getParameter("endDate");
//            l = pChecker.checkInputVals(startDate, endDate) ? passStationService.getAllCellPerPcsHistory(
//                    pChecker.checkInputVal(minPcs) == false ? 0 : Integer.parseInt(minPcs),
//                    pChecker.checkInputVal(maxPcs) == false ? Integer.MAX_VALUE : Integer.parseInt(maxPcs), 
//                    startDate, 
//                    endDate
//            ) : new ArrayList();
//        }
        out.print(jsonObject.put("data", l == null ? new ArrayList() : l));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
