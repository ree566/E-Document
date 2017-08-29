/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    private final String filePath = "C:\\Users\\Wei.Cheng\\Desktop\\worktime-template(6).xls";
    private final String cloneFilePath = "C:\\Users\\Wei.Cheng\\Desktop\\poi-test.xls";

    @Test
    public void test() throws Exception {
        Workbook wb;
        try (FileInputStream excelFile = new FileInputStream(new File(cloneFilePath))) {
            wb = new HSSFWorkbook(excelFile);
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Test34");
        sheet.protectSheet("s3cr3t1");
        
        fileExport(wb);
    }

    private void testCreateEncryptSheet() throws Exception {
        Workbook wb = new HSSFWorkbook();

        Sheet sheet1 = wb.createSheet("Sheet1");
        sheet1.protectSheet("s3cr3t");

        fileExport(wb);
    }
    
    private void fileExport(Workbook wb) throws IOException{
        try (FileOutputStream outputStream = new FileOutputStream(cloneFilePath)) {
            wb.write(outputStream);
            wb.close();
        }
    }

}
