/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.BasicService;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) throws Exception {
//        LineService ls = BasicService.getLineService();
//        List<Line> l = ls.getLine();

        List<Map> l = BasicService.getBabService().getClosedBABInfo("16/09/01", "16/09/12");
        Map firstData = l.get(0); // Get the first data to read the object paramaters.

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet spreadsheet = workbook.createSheet("employe db");
        HSSFRow row = spreadsheet.createRow(0);
        HSSFCell cell;
        Iterator it = firstData.keySet().iterator();
        int loopCount = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            cell = row.createCell(loopCount);
            cell.setCellValue(key);
            loopCount++;
        }

        int x = 1;
        int y = 0;
        for (Map m : l) {
            it = m.keySet().iterator();
            row = spreadsheet.createRow(x);
            while (it.hasNext()) {
                Object key = it.next();
                cell = row.createCell(y);
                cell.setCellValue(m.get(key).toString());
                y++;
            }
            y = 0;
            x++;
        }
        FileOutputStream out = new FileOutputStream(
                new File(System.getProperty("user.home") + "\\Desktop\\project\\" + "exceldatabase.xls"));
        workbook.write(out);
        out.close();
        System.out.println(
                "exceldatabase.xlsx written successfully");

//        System.out.println(new Gson().toJson());
    }

}
