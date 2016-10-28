/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.UserSelectFilter;
import com.advantech.model.BasicDAO;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import static java.lang.System.out;
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

    private final int minAllowAmount = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

//http://stackoverflow.com/questions/13853300/jquery-file-download-filedownload
//personalAlm記得轉格式才能special Excel generate
        String lineType = req.getParameter("lineType");
        String sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String aboveStandard = req.getParameter("aboveStandard");

        CountermeasureService cs = BasicService.getCountermeasureService();
        List<Map> countermeasures = cs.getCountermeasure(startDate, endDate);
        List<Map> personalAlarms = cs.getPersonalAlm(startDate, endDate);

        UserSelectFilter usf = new UserSelectFilter().setList(countermeasures);
        UserSelectFilter usf2 = new UserSelectFilter().setList(personalAlarms);

        out.println(aboveStandard);
        boolean isAbove = Boolean.parseBoolean(aboveStandard);
        out.println(isAbove);

        if (!"-1".equals(lineType)) {
            usf.filterData("lineType", lineType);
            usf2.filterData("lineType", lineType);
        }

        if (!"-1".equals(sitefloor)) {
            usf.filterData("sitefloor", sitefloor);
            usf2.filterData("sitefloor", sitefloor);
        }

        if (isAbove) {
            usf.greaterThan("測量數量", minAllowAmount);
        }

        List list = usf.getList();
        List list2 = cs.transformPersonalAlmDataPattern(usf2.getList());//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)

        if (list.isEmpty() || (list.isEmpty() && list2.isEmpty())) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            Workbook w = this.generateBabDetailIntoExcel(list, list2, isAbove);
            String fileExt = ExcelGenerator.getFileExt(w);

            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Set-Cookie", "fileDownload=true; path=/");
            res.setHeader("Content-Disposition",
                    "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
            w.write(res.getOutputStream());
            w.close();
        }
    }

    public Workbook generateBabDetailIntoExcel(List<Map> countermeasures, List<Map> personalAlarms, boolean showAboveOnly) {

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
        if (!showAboveOnly && !sheet2Data.isEmpty() && !sheet4Data.isEmpty()) {
            generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.generateWorkBooks(sheet2Data);
            generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.appendSpecialPattern(sheet4Data);
        }
        return generator.getWorkbook();
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        String lineType = "Packing", sitefloor = "-1";
        String startDate = "16/10/28", endDate = "16/10/28";
        CountermeasureService cs = BasicService.getCountermeasureService();
        List<Map> countermeasures = cs.getCountermeasure(startDate, endDate);
        List<Map> personalAlarms = cs.getPersonalAlm(startDate, endDate);

        UserSelectFilter usf = new UserSelectFilter().setList(countermeasures);
        UserSelectFilter usf2 = new UserSelectFilter().setList(cs.transformPersonalAlmDataPattern(personalAlarms));

        if (!"-1".equals(lineType)) {
            usf.filterData("lineType", lineType);
            usf2.filterData("lineType", lineType);
        }

        if (!"-1".equals(sitefloor)) {
            usf.filterData("sitefloor", sitefloor);
            usf2.filterData("sitefloor", sitefloor);
        }

        ExcelGenerator generator = new ExcelGenerator();
        generator.createExcelSheet("t1");
        generator.generateWorkBooks(usf.getList());
        generator.createExcelSheet("t2");
        generator.appendSpecialPattern(usf2.getList());
        generator.outputExcel("TT");
    }
}
