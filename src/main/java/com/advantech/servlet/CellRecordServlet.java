/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.CellService;
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

    private CellService cellService;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        cellService = BasicService.getCellService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String PO = req.getParameter("PO");
        String topN = req.getParameter("topN");

        JSONObject jsonObject = new JSONObject();

        List l = null;
        if (pChecker.checkInputVal(PO)) {
            //equals false = not setting topN filter
            l = pChecker.checkInputVal(topN) == false ? cellService.getCellPerPcsHistory(PO) : cellService.getCellPerPcsHistory(PO, Integer.parseInt(topN));
        }
        out.print(jsonObject.put("data", l == null ? new ArrayList() : l));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
