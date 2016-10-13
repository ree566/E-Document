/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.BasicDAO;
import com.advantech.service.BasicService;
import com.advantech.service.FBNService;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import static java.lang.System.out;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ExcelGenerator {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

    private static HSSFWorkbook workbook;
    private static int sheetNum = 1;

    //Set values
    private static int xIndex = 0, yIndex = 0;

    private static void init() {
        sheetNum = 1;
        xIndex = 0;
        yIndex = 0;
        log.info("New one workbook");
        workbook = new HSSFWorkbook();
    }

    private static HSSFSheet createExcelSheet() {
        return workbook.createSheet("sheet" + (sheetNum++));
    }

    public static HSSFWorkbook generateWorkBook(List<Map>... data) {
        init();
        for (List<Map> l : data) {
            generateWorkBook(l);
        }
        return workbook;
    }

    private static HSSFWorkbook generateWorkBook(List<Map> data) {
        HSSFSheet spreadsheet = createExcelSheet();
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

    private static void excelGenerate() {
        excelGenerate(getYesterday());
    }

    /**
     * Date in yyyy-MM-dd
     *
     * @param date
     */
    private static void excelGenerate(String date) {
        FBNService fService = BasicService.getFbnService();
        List<Map> babList = BasicService.getBabService().getBABForMap(date);
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet spreadsheet = wb.createSheet("test");

        int dataCount = 0;

        for (Map bab : babList) {
            dataCount++;

            int BABid = (int) bab.get("id");
            List<Map> abnormalDataTotal = fService.getTotalAbnormalData(BABid);
            List<Map> abnormalData = fService.getAbnormalData(BABid);

            //Make sure the data if empty or not(deadLock aways happend).
            if (abnormalDataTotal.isEmpty()) {
                abnormalDataTotal = fService.getTotalAbnormalData(BABid);
            }

            if (abnormalData.isEmpty()) {
                abnormalData = fService.getAbnormalData(BABid);
            }

            HSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(dataCount % 2 == 0 ? HSSFColor.LIGHT_TURQUOISE.index : HSSFColor.LIGHT_GREEN.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            setData(spreadsheet, style, bab);
            yIndex++;
            spreadsheet.createRow(yIndex);

            setData(spreadsheet, style, abnormalDataTotal);
            yIndex++;
            spreadsheet.createRow(yIndex);

            setData(spreadsheet, style, abnormalData);

            yIndex += 2;
            spreadsheet.createRow(yIndex);
        }

        outputExcel(wb, date);
    }

    private static HSSFSheet setData(HSSFSheet spreadsheet, HSSFCellStyle style, Map m) {
        List l = new ArrayList();
        l.add(m);
        return setData(spreadsheet, style, l);
    }

    private static HSSFSheet setData(HSSFSheet spreadsheet, HSSFCellStyle style, List<Map> l) {
        HSSFRow row = spreadsheet.createRow(yIndex);
        HSSFCell cell;
        if (!l.isEmpty()) {
            Map firstData = l.get(0);
            //Set the header
            Iterator it = firstData.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                cell = row.createCell(xIndex);
                cell.setCellValue(key);
                cell.setCellStyle(style);
                xIndex++;
            }
            xIndex = 0;
            yIndex++;

            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(yIndex);
                while (it.hasNext()) {
                    cell = row.createCell(xIndex);
                    cell.setCellStyle(style);
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
                    xIndex++;
                }
                xIndex = 0;//Reset the cell index and begin next data line insert.
                yIndex++;

            }
        } else {
            row = spreadsheet.createRow(yIndex);
            cell = row.createCell(xIndex);
            cell.setCellStyle(style);
            cell.setCellValue("Abnormal data is empty.");
            yIndex++;
        }
        return spreadsheet;
    }

    private static void outputExcel(Workbook w, String fileName) {
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        fileName += ".xls";
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filePath + fileName);
            w.write(fileOut);
            fileOut.close();
            out.println("Excel generate success");
        } catch (Exception ex) {
            out.println(ex.toString());
        }
    }

    private static String getToday() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        return fmt.print(new DateTime());
    }

    private static String getYesterday() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        return fmt.print(new DateTime().minusDays(1));
    }

    private static String dateFormatToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit();
        excelGenerate();
    }

}
