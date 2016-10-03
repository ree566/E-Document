/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.ExcelGenerator;
import com.advantech.service.BasicService;
import com.advantech.service.CountermeasureService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABExcelGenerate", urlPatterns = {"/BABExcelGenerate"})
public class BABExcelGenerate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-Disposition",
                "attachment; filename=sampleData" + DateTimeFormat.forPattern("yyyyMMdd").print(new DateTime()) + ".xls");

        String lineType = req.getParameter("lineType");
        String sitefloor = req.getParameter("sitefloor");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        CountermeasureService cs = BasicService.getCountermeasureService();
        List countermeasures = cs.getCountermeasureView(lineType, sitefloor, startDate, endDate);
        List personalAlms = cs.getPersonalAlm(lineType, sitefloor, startDate, endDate);

        HSSFWorkbook w = ExcelGenerator.generateWorkBook(countermeasures, personalAlms);

        w.write(res.getOutputStream());
        w.close();
    }

}
