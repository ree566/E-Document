/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Cell過站資訊匯出excel
 */
package com.advantech.servlet;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.PassStationService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Workbook;
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

        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String PO = req.getParameter("PO");
        String lineId = req.getParameter("lineId");
        String minPcs = req.getParameter("minPcs");
        String maxPcs = req.getParameter("maxPcs");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String action = req.getParameter("action");

        JSONObject jsonObject = new JSONObject();

        List l = null;

        l = passStationService.getAllCellPerPcsHistory(
                pChecker.checkInputVal(PO) ? PO : null,
                pChecker.checkInputVal(lineId) ? Integer.parseInt(lineId) : null,
                pChecker.checkInputVal(minPcs) ? Integer.parseInt(minPcs) : null,
                pChecker.checkInputVal(maxPcs) ? Integer.parseInt(maxPcs) : null,
                pChecker.checkInputVal(startDate) ? startDate : null,
                pChecker.checkInputVal(endDate) ? endDate : null
        );

        switch (action) {
            case "SELECT":
                res.setContentType("application/json");
                PrintWriter out = res.getWriter();
                out.print(jsonObject.put("data", l == null ? new ArrayList() : l));
                break;
            case "FILE_DOWNLOAD":
                res.setContentType("text/plain");
                String[] columnHeader = req.getParameterValues("columnHeader[]");

                if (columnHeader == null || columnHeader.length == 0) {
                    res.getWriter().println("fail");
                } else {
                    ExcelGenerator generator = new ExcelGenerator();
                    generator.createExcelSheet();
                    generator.addSkipDecimalFormatIndex(3, 4);
                    generator.specifyColumnHeaders(columnHeader);
                    generator.generateWorkBooks(l);

                    Workbook w = generator.getWorkbook();

                    String fileExt = ExcelGenerator.getFileExt(w);

                    res.setContentType("application/vnd.ms-excel");
                    res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                    res.setHeader("Content-Disposition",
                            "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                    w.write(res.getOutputStream());
                    w.close();
                }
                break;
        }
    }

}
