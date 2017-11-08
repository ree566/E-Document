/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.service.CellService;
import com.advantech.service.PassStationService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class CellController {

    @Autowired
    private CellService cellService;

    @Autowired
    private PassStationService passStationService;

    @RequestMapping(value = "/CellRecordServlet", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam(required = false, value = "PO") String po,
            @RequestParam(required = false, value = "type") String type,
            @RequestParam(required = false, value = "lineId") Integer lineId,
            @RequestParam(required = false, value = "minPcs") Integer minPcs,
            @RequestParam(required = false, value = "maxPcs") Integer maxPcs,
            @RequestParam(required = false, value = "startDate") String startDate,
            @RequestParam(required = false, value = "endDate") String endDate,
            @RequestParam(required = false, value = "action") String action,
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {

        JSONObject jsonObject = new JSONObject();

        List l = passStationService.getAllCellPerPcsHistory(po, type, lineId, minPcs, maxPcs, startDate, endDate);

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

    @RequestMapping(value = "/CellSearch", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam(required = false, value = "lineId") Integer lineId,
            @RequestParam(required = false, value = "action") String action,
            @RequestParam(required = false, value = "startDate") String startDate,
            @RequestParam(required = false, value = "endDate") String endDate,
            HttpServletRequest req,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        if (action != null) {
            switch (action) {
                case "getHistory":
                    List l;
                    if (startDate != null && endDate != null) {
                        l = cellService.cellHistoryView(startDate, endDate);
                    } else {
                        l = cellService.cellHistoryView();
                    }
                    out.print(new JSONObject().put("data", l));
                    break;
                case "getProcessing":
                    if (lineId != null) {
                        out.print(new JSONObject().put("data", cellService.getCellProcessing(lineId)));
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
