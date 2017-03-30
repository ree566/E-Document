/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.endpoint.Endpoint6;
import com.advantech.entity.BAB;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.helper.JsonHelper;
import java.io.*;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String PO = req.getParameter("PO");
        String line = req.getParameter("line");
        BAB b = new BAB(PO, "test", Integer.parseInt(line), 2, 1);
        Endpoint6.addNewBab(b);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        String jsonObject = req.getParameter("columnData");

        JSONArray arr = new JSONArray(jsonObject);

        List<Map> l = JsonHelper.toList(arr);

        ExcelGenerator generator = new ExcelGenerator();
        generator.createExcelSheet();
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
}
