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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Wei.Cheng
 */
public class XlsWorkBook {

    private Map<String, XlsWorkSheet> _sheets;

    public XlsWorkBook(String fileName) throws IOException {
        this(new FileInputStream(new File(fileName)));
    }

    public XlsWorkBook(InputStream in) throws IOException {
        _sheets = new HashMap<>();

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            XlsWorkSheet sheet = new XlsWorkSheet(workbook.getSheetName(i),
                    workbook.getSheetAt(i));
            _sheets.put(workbook.getSheetName(i), sheet);
        }

    }

    public XlsWorkSheet getSheet(String sheetName) {
        return _sheets.get(sheetName);
    }

}
