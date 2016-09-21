/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ExcelGenerator {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

    private static HSSFWorkbook workbook;

    public static HSSFWorkbook generateWorkBook(List<Map> data) {
        workbook = new HSSFWorkbook();
        HSSFSheet spreadsheet = workbook.createSheet("dataTable");
        HSSFRow row = spreadsheet.createRow(0);
        HSSFCell cell;
        if (!data.isEmpty()) {
            Map firstData = data.get(0);
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
            for (Map m : data) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(x);
                while (it.hasNext()) {
                    cell = row.createCell(y);
                    Object key = it.next();
                    Object value = m.get(key);
                    if (value instanceof Clob) {
                        cell.setCellValue(clobToString((Clob) value));
                    } else if (value instanceof java.util.Date) {
                        cell.setCellValue(dateFormatToString((Date) value));
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                        createFloatCell(cell);
                    } else if (value instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) value).doubleValue());
                        createFloatCell(cell);
                    } else if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    y++;
                }
                y = 0;//Reset the cell index and begin next data line insert.
                x++;
            }
        }
        return workbook;
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
            log.error(e.toString());
        }
        return sb.toString();
    }

    private static HSSFCell createFloatCell(HSSFCell cell) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        cell.setCellStyle(style);
        return cell;
    }

    private static String dateFormatToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }
}
