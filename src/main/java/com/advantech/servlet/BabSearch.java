/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.entity.PrepareSchedule;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.PrepareScheduleService;
import com.google.gson.Gson;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BabSearch", urlPatterns = {"/BabSearch"})
public class BabSearch extends HttpServlet {

    private BABService babService = null;
    private PrepareScheduleService prepareScheduleService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        babService = BasicService.getBabService();
        prepareScheduleService = BasicService.getPrepareScheduleService();
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
        String po = req.getParameter("po");
        String saveLine = req.getParameter("saveline");
        String poGetBAB = req.getParameter("po_getBAB");
        String poSaveLine = req.getParameter("po_saveline");
        if (pChecker.checkInputVal(po)) {
            PrepareSchedule schedule = prepareScheduleService.getPrepareSchedule(po);
            out.print(schedule == null ? "data not found" : convertString(schedule.getModel_name()));
        } else if (pChecker.checkInputVal(saveLine)) {
            out.print(new Gson().toJson(babService.getProcessingBABByLine(Integer.parseInt(saveLine))));
        } else if (pChecker.checkInputVals(poGetBAB, poSaveLine)) {
            out.print(babService.getBABInfoWithSensorState(poGetBAB, poSaveLine));
        }
    }

    private String convertString(String input) {
        String converstring = "";
        Pattern p = Pattern.compile("[\\w|-]");
        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            converstring += matcher.group();
        }
        return converstring;
    }

}
