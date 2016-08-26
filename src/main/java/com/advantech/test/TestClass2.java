/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.Countermeasure;
import com.advantech.service.BasicService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {
    
    private static final Logger log = LoggerFactory.getLogger(TestClass2.class);

    public static void main(String arg0[]) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        List<Countermeasure> l = BasicService.getCountermeasureService().getCountermeasure();

        int rownum = 0;
        for (Countermeasure c : l) {
            Row row = sheet.createRow(rownum++);
            row.createCell(1).setCellValue(c.getBABid());
            row.createCell(1).setCellValue(c.getEditor());
        }

        try {
            File f = new File("D:\\new.xls");
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, false);
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}
