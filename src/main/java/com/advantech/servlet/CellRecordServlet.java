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
import com.advantech.service.PassStationService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@Controller
public class CellRecordServlet {

    @Autowired
    private PassStationService passStationService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/CellRecordServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String PO = req.getParameter("PO");
        String type = req.getParameter("type");
        String lineId = req.getParameter("lineId");
        String minPcs = req.getParameter("minPcs");
        String maxPcs = req.getParameter("maxPcs");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String action = req.getParameter("action");

        JSONObject jsonObject = new JSONObject();

        List l = passStationService.getAllCellPerPcsHistory(
                pChecker.checkInputVal(PO) ? PO : null,
                pChecker.checkInputVal(type) ? type : null,
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

                    try (Workbook w = generator.getWorkbook()) {
                        String fileExt = ExcelGenerator.getFileExt(w);

                        res.setContentType("application/vnd.ms-excel");
                        res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        res.setHeader("Content-Disposition",
                                "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                        w.write(res.getOutputStream());
                    }
                }
                break;
        }
    }

}
