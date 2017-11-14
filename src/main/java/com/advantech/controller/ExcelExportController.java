/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.UserSelectFilter;
import com.advantech.service.SystemReportService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class ExcelExportController {
    
    @Autowired
    private SystemReportService reportService;

    private final int minAllowAmount = 10;

    private String lineType, sitefloor;

    @RequestMapping(value = "/BABExcelForEfficiencyReport", method = {RequestMethod.GET})
    @ResponseBody
    protected void babExcelForEfficiencyReport(
            @RequestParam String lineType,
            @RequestParam String sitefloor,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam boolean aboveStandard,
            HttpServletResponse res)
            throws IOException {

        this.lineType = lineType;
        this.sitefloor = sitefloor;

        List<Map> data = fitData(reportService.getCountermeasureAndPersonalAlm(startDate, endDate));
        List<Map> emptyRecords = fitData(reportService.getEmptyRecordDownExcel(startDate, endDate));

        List list2 = transformEfficiencyReportPattern(data);//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)
        List list3 = emptyRecords;

        if (data.isEmpty()) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            try (Workbook w = this.generateBabDetailIntoExcel(list2, list3, aboveStandard)) {
                String fileExt = ExcelGenerator.getFileExt(w);

                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                res.setHeader("Content-Disposition",
                        "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                w.write(res.getOutputStream());
            }
        }
    }

    @RequestMapping(value = "/BABExcelGenerate", method = {RequestMethod.GET})
    @ResponseBody
    protected void babExcelGenerate(
            @RequestParam String lineType,
            @RequestParam String sitefloor,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam boolean aboveStandard,
            HttpServletResponse res) throws IOException {

        this.lineType = lineType;
        this.sitefloor = sitefloor;

        //http://stackoverflow.com/questions/13853300/jquery-file-download-filedownload
        //personalAlm記得轉格式才能special Excel generate
        List<Map> countermeasures = fitData(reportService.getCountermeasureForExcel(startDate, endDate));
        List<Map> personalAlarms = fitData(reportService.getPersonalAlmForExcel(startDate, endDate));
        List<Map> emptyRecords = fitData(reportService.getEmptyRecordDownExcel(startDate, endDate));

        List list = countermeasures;
        List list2 = transformPersonalAlmDataPattern(personalAlarms);//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)
        List list3 = emptyRecords;

        if (list.isEmpty() || (list.isEmpty() && list2.isEmpty())) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            try (Workbook w = this.generateBabDetailIntoExcel(list, list2, list3, aboveStandard)) {
                String fileExt = ExcelGenerator.getFileExt(w);

                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                res.setHeader("Content-Disposition",
                        "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                w.write(res.getOutputStream());
            }
        }
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

    private Workbook generateBabDetailIntoExcel(List<Map> countermeasures, List<Map> personalAlarms, List<Map> emptyRecords, boolean showAboveOnly) {

        String filterColumnName = "測量數量";
        String countermeasureSheetName = "異常填寫";
        String personalAlmSheetName = "亮燈頻率";
        String lastSheetName = "無儲存紀錄工單列表";

        List<Map> sheet1Data = new ArrayList(), sheet2Data = new ArrayList(), sheet3Data, sheet4Data = new ArrayList();
        List<Map> lastSheetData = emptyRecords;

        for (Map m : countermeasures) {
            if (m.containsKey(filterColumnName)) {
                if ((Integer) m.get(filterColumnName) >= minAllowAmount) {
                    sheet1Data.add(m);
                } else {
                    sheet2Data.add(m);
                }
            }
        }

        Iterator it = personalAlarms.iterator();
        while (it.hasNext()) {
            Map personalAlarm = (Map) it.next();
            for (Map cm : sheet2Data) {
                if (Objects.equals(cm.get("id"), personalAlarm.get("id"))) {
                    sheet4Data.add(personalAlarm);
                    it.remove();
                    break;
                }
            }
        }
        sheet3Data = personalAlarms;

        ExcelGenerator generator = new ExcelGenerator();

        generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "≧" + minAllowAmount + "台)");
        generator.generateWorkBooks(sheet1Data);

        generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "≧" + minAllowAmount + "台)");
        generator.appendSpecialPattern(sheet3Data, 9, "Z", "瓶頸站", "AA", "亮燈頻率");

        if (!showAboveOnly && !sheet2Data.isEmpty() && !sheet4Data.isEmpty()) {
            generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.generateWorkBooks(sheet2Data);
            generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.appendSpecialPattern(sheet4Data, 9, "Z", "瓶頸站", "AA", "亮燈頻率");
        }

        generator.createExcelSheet(lastSheetName);
        generator.generateWorkBooks(lastSheetData);

        return generator.getWorkbook();
    }

    public List<Map> transformPersonalAlmDataPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPercent(Personal)";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈頻率";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

    private Map removeUnusedKeyInMap(Map m, String... keys) {
        for (String st : keys) {
            m.remove(st);
        }
        return m;
    }
    //--------效率報表------------------------------

    public List<Map> transformEfficiencyReportPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPcs";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈次數";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

}
