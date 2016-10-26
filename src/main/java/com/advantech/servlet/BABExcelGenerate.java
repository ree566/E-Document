/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABExcelGenerate", urlPatterns = {"/BABExcelGenerate"})
public class BABExcelGenerate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

//http://stackoverflow.com/questions/13853300/jquery-file-download-filedownload
        String lineType = req.getParameter("lineType");
        String sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        CountermeasureService cs = BasicService.getCountermeasureService();
        List countermeasures = cs.getCountermeasure(lineType, sitefloor, startDate, endDate);
        List personalAlarms = cs.getPersonalAlm(lineType, sitefloor, startDate, endDate);

        if (countermeasures.isEmpty() || (countermeasures.isEmpty() && personalAlarms.isEmpty())) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            Workbook w = this.generateBabDetailIntoExcel(countermeasures, personalAlarms);
            String fileExt = ExcelGenerator.getFileExt(w);

            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Set-Cookie", "fileDownload=true; path=/");
            res.setHeader("Content-Disposition",
                    "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
            w.write(res.getOutputStream());
            w.close();
        }
    }

    public Workbook generateBabDetailIntoExcel(List<Map> countermeasures, List<Map> personalAlarms) {
        int minAllowAmount = 10;
        String filterColumnName = "測量數量";
        String countermeasureSheetName = "異常填寫";
        String personalAlmSheetName = "亮燈頻率";

        List<Map> sheet1Data = new ArrayList(), sheet2Data = new ArrayList(), sheet3Data, sheet4Data = new ArrayList();

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
                if (Objects.equals((Integer) cm.get("id"), (Integer) personalAlarm.get("id"))) {
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
        generator.appendSpecialPattern(sheet3Data);
        generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
        generator.generateWorkBooks(sheet2Data);
        generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
        generator.appendSpecialPattern(sheet4Data);

        return generator.getWorkbook();
    }
}
