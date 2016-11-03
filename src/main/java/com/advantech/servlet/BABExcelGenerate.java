/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.entity.ActionCodeMapping;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.PropertiesReader;
import com.advantech.helper.UserSelectFilter;
import com.advantech.model.BasicDAO;
import com.advantech.service.ActionCodeMappingService;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import com.advantech.service.ModelResponsorService;
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
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABExcelGenerate", urlPatterns = {"/BABExcelGenerate"})
public class BABExcelGenerate extends HttpServlet {

    private final int minAllowAmount = 10;

    private final CountermeasureService cService = BasicService.getCountermeasureService();
    private final ActionCodeMappingService amService = BasicService.getActionCodeMappingService();
    private final ModelResponsorService mrService = BasicService.getModelResponsorService();
    private final List<ActionCodeMapping> mappingList = amService.getActionCodeMapping();
    private final JSONObject responseUserPerLine = PropertiesReader.getInstance().getResponseUserPerLine();
    private final List<Map> modelResponsor = mrService.getModelResponsor1();
//    private final JSONObject responseUserPerLine = PropertiesReader.getInstance().getResponseUserPerLine();
//    private final UserSelectFilter actionCodeMapping = new UserSelectFilter().setList(amService.getActionCodeMapping1());
//    private final UserSelectFilter modelResponsorList = new UserSelectFilter().setList(mrService.getModelResponsor1());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        DateTime startTime = new DateTime();

//http://stackoverflow.com/questions/13853300/jquery-file-download-filedownload
//personalAlm記得轉格式才能special Excel generate
        String lineType = req.getParameter("lineType");
        String sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String aboveStandard = req.getParameter("aboveStandard");

        List<Map> countermeasures = cService.getCountermeasureForExcel(startDate, endDate);
        List<Map> personalAlarms = cService.getPersonalAlmForExcel(startDate, endDate);

        UserSelectFilter usf1 = new UserSelectFilter().setList(countermeasures);
        UserSelectFilter usf2 = new UserSelectFilter().setList(personalAlarms);

        boolean isAbove = Boolean.parseBoolean(aboveStandard);

        if (!"-1".equals(lineType)) {
            usf1.filterData("lineType", lineType);
            usf2.filterData("lineType", lineType);
        }

        if (!"-1".equals(sitefloor)) {
            usf1.filterData("sitefloor", sitefloor);
            usf2.filterData("sitefloor", sitefloor);
        }

        List list = usf1.getList();
        List list2 = cService.transformPersonalAlmDataPattern(usf2.getList());//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)

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

        DateTime endTime = new DateTime();
        out.println("Total time processing: " + (Seconds.secondsBetween(startTime, endTime).getSeconds()) + " SEC.");
    }

    private Workbook generateBabDetailIntoExcel(List<Map> countermeasures, List<Map> personalAlarms, boolean showAboveOnly) {

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
        List<Map> countermeasures = cs.getCountermeasureForExcel(startDate, endDate);
        List<Map> personalAlarms = cs.getPersonalAlmForExcel(startDate, endDate);

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
