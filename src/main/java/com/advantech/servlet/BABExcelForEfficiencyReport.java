/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 效率報表匯出用 (For MFG)
 */
package com.advantech.servlet;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.UserSelectFilter;
import com.advantech.service.BABService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BABExcelForEfficiencyReport{

    @Autowired
    private CountermeasureService cService;
    
    @Autowired
    private BABService babService;

    private final int minAllowAmount = 10;

    private String lineType, sitefloor;

    @RequestMapping(value = "/BABExcelForEfficiencyReport", method = {RequestMethod.GET})
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        DateTime startTime = new DateTime();

        lineType = req.getParameter("lineType");
        sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String aboveStandard = req.getParameter("aboveStandard");

        List<Map> data = fitData(cService.getCountermeasureAndPersonalAlm(startDate, endDate));
        List<Map> emptyRecords = fitData(babService.getEmptyRecordDownExcel(startDate, endDate));

        List list2 = cService.transformEfficiencyReportPattern(data);//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)
        List list3 = emptyRecords;

        boolean isAbove = Boolean.parseBoolean(aboveStandard);

        if (data.isEmpty()) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            try (Workbook w = this.generateBabDetailIntoExcel(list2, list3, isAbove)) {
                String fileExt = ExcelGenerator.getFileExt(w);

                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                res.setHeader("Content-Disposition",
                        "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                w.write(res.getOutputStream());
            }
        }

        DateTime endTime = new DateTime();
        out.println("Total time processing: " + (Seconds.secondsBetween(startTime, endTime).getSeconds()) + " SEC.");
    }

    private List<Map> fitData(List data) {
        UserSelectFilter usf = new UserSelectFilter().setList(data);

        if (!"-1".equals(lineType)) {
            usf.filterData("lineType", lineType);
        }

        if (!"-1".equals(sitefloor)) {
            usf.filterData("sitefloor", Integer.parseInt(sitefloor));
        }

        return usf.getList();
    }

    private Workbook generateBabDetailIntoExcel(List<Map> data, List<Map> emptyRecords, boolean showAboveOnly) {

        String filterColumnName = "測量數量";
        String countermeasureSheetName = "異常填寫";
        String lastSheetName = "無儲存紀錄工單列表";

        List<Map> sheet1Data = new ArrayList(), sheet2Data = new ArrayList();
        List<Map> lastSheetData = emptyRecords;

        for (Map m : data) {
            if (m.containsKey(filterColumnName)) {
                if ((Integer) m.get(filterColumnName) >= minAllowAmount) {
                    sheet1Data.add(m);
                } else {
                    sheet2Data.add(m);
                }
            }
        }

        ExcelGenerator generator = new ExcelGenerator();

        generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "≧" + minAllowAmount + "台)");
        generator.appendSpecialPattern(sheet1Data, 18, "AM", "瓶頸站", "AN", "亮燈次數");

        if (!showAboveOnly && !sheet2Data.isEmpty()) {
            generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.appendSpecialPattern(sheet2Data, 18, "AM", "瓶頸站", "AN", "亮燈次數");
        }

        generator.createExcelSheet(lastSheetName);
        generator.generateWorkBooks(lastSheetData);

        return generator.getWorkbook();
    }

}
