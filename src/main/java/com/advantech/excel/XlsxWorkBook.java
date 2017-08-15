/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Wei.Cheng
 */
public class XlsxWorkBook implements AutoCloseable {

    private final XSSFWorkbook workbook;
    private Map<String, XlsxWorkSheet> _sheets;

    public XlsxWorkBook(String fileName) throws Exception {
        this(new FileInputStream(new File(fileName)));
    }

    public XlsxWorkBook(InputStream in) throws Exception {
        _sheets = new HashMap<>();
 
        workbook = (XSSFWorkbook) WorkbookFactory.create(in);
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            XlsxWorkSheet sheet = new XlsxWorkSheet(workbook.getSheetName(i),
                    workbook.getSheetAt(i));
            _sheets.put(workbook.getSheetName(i), sheet);
        }

        if (in != null) {
            in.close();
        }
    }

    public XlsxWorkSheet getSheet(String sheetName) {
        return _sheets.get(sheetName);
    }

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
            System.out.println("Close excel.");
        }

        if (_sheets != null) {
            _sheets.clear();
            System.out.println("Clear sheets map.");
        }
    }

}
