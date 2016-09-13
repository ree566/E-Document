/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.service.BasicService;
import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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

        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-Disposition",
                "attachment; filename=sampleData" + DateTimeFormat.forPattern("yyyyMMdd").print(new DateTime()) + ".xls");

        List<Map> l = BasicService.getCountermeasureService().getCountermeasureView();
        Map firstData = l.get(0);//Get first object to set the header value.

        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet spreadsheet = workbook.createSheet("dataTable");
            HSSFRow row = spreadsheet.createRow(0);
            HSSFCell cell;
            
            //Set the header
            Iterator it = firstData.keySet().iterator();
            int loopCount = 0;
            while (it.hasNext()) {
                String key = (String) it.next();
                cell = row.createCell(loopCount);
                cell.setCellValue(key);
                loopCount++;
            }

            //Set values
            int x = 1, y = 0;
            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(x);
                while (it.hasNext()) {
                    cell = row.createCell(y);
                    Object key = it.next();
                    Object value = m.get(key);
                    if (value instanceof Clob) {
                        value = clobToString((Clob) value);
                    }
                    cell.setCellValue(value.toString());
                    y++;
                }
                y = 0;
                x++;
            }

            workbook.write(res.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        out.println(BasicService.getBabService().getAvg(Integer.parseInt(id)));
    }

    private static String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
            br.close();
        } catch (SQLException | IOException e) {
            // handle this exception
        }
        // handle this exception
        return sb.toString();
    }
}
