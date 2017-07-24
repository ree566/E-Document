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
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Wei.Cheng
 */
public class XlsWorkBook {

    private Map<String, XlsWorkSheet> _sheets;

    public XlsWorkBook(String fileName) throws Exception {
        this(new FileInputStream(new File(fileName)));
    }

    public XlsWorkBook(InputStream in) throws Exception {
        _sheets = new HashMap<>();

        HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(in);
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            XlsWorkSheet sheet = new XlsWorkSheet(workbook.getSheetName(i),
                    workbook.getSheetAt(i));
            _sheets.put(workbook.getSheetName(i), sheet);
        }

        if (in != null) {
            in.close();
        }
    }

    public XlsWorkSheet getSheet(String sheetName) {
        return _sheets.get(sheetName);
    }

}
