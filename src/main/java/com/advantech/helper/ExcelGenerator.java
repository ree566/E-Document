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

    private static final DatetimeGenerator dg = new DatetimeGenerator("E yyyy/MM/dd HH:mm");

    //Set values
    private static int xIndex = 0, yIndex = 0;

    private static void init() {
        sheetNum = 1;
        xIndex = 0;
        yIndex = 0;
        workbook = new HSSFWorkbook();
        log.info("New one workbook");
    }

    private static HSSFSheet createExcelSheet() {
        return workbook.createSheet("sheet" + (sheetNum++));
    }

    public static HSSFWorkbook generateWorkBooks(List<Map>... data) {
        init();
        for (List<Map> l : data) {
            generateWorkBook(l);
        }
        return workbook;
    }

    private static HSSFWorkbook generateWorkBook(List<Map> data) {
        HSSFSheet spreadsheet = createExcelSheet();
        HSSFRow row = spreadsheet.createRow(0);
        if (!data.isEmpty()) {
            Map firstData = data.get(0);
            //Set the header
            Iterator it = firstData.keySet().iterator();
            int loopCount = 0;
            while (it.hasNext()) {
                row.createCell(loopCount++).setCellValue((String) it.next());
            }

            //Set values
            int x = 1, y = 0;
            for (Map m : data) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(x++);
                while (it.hasNext()) {
                    setCellValue(row.createCell(y++), m.get(it.next()));
                }
                y = 0;//Reset the cell index and begin next data line insert.
            }
        }
        return workbook;
    }

    private static HSSFCell setCellValue(HSSFCell cell, Object value) {
        if (value instanceof Clob) {
            cell.setCellValue(clobToString((Clob) value));
        } else if (value instanceof java.util.Date) {
            cell.setCellValue(dg.dateFormatToString((Date) value));
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
        return cell;
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
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        cell.setCellStyle(style);
        return cell;
    }

    public static void formatExcel() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            HSSFRow row = workbook.getSheetAt(i).getRow(0);
            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                workbook.getSheetAt(0).autoSizeColumn(colNum);
            }
        }
    }

    public static void outputExcel(Workbook w, String fileName) {
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

    //客製化style的excel---------------------------------------------------------
    private static void sensorAbnormalDataGenerate() {
        //異常Sensor Data generate
        sensorAbnormalDataGenerate(new DatetimeGenerator("yyyy-MM-dd").getYesterday());
    }

    /**
     * Date in yyyy-MM-dd
     *
     * @param date
     */
    private static void sensorAbnormalDataGenerate(String date) {
        FBNService fService = BasicService.getFbnService();
        List<Map> babList = BasicService.getBabService().getBABForMap(date);
        workbook = new HSSFWorkbook();

        HSSFSheet spreadsheet = workbook.createSheet("test");

        int dataCount = 0;

        for (Map bab : babList) {
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

            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(dataCount++ % 2 == 0 ? HSSFColor.LIGHT_TURQUOISE.index : HSSFColor.LIGHT_GREEN.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            setData(spreadsheet, style, bab);
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalDataTotal);
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalData);
            spreadsheet.createRow(yIndex += 2);
        }
        outputExcel(workbook, date);
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
                cell = row.createCell(xIndex++);
                cell.setCellValue(key);
                cell.setCellStyle(style);
            }

            xIndex = 0;
            yIndex++;

            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(yIndex++);
                while (it.hasNext()) {
                    cell = row.createCell(xIndex++);
                    cell.setCellStyle(style);
                    setCellValue(cell, m.get(it.next()));
                }
                xIndex = 0;//Reset the cell index and begin next data line insert.
            }
        } else {
            row = spreadsheet.createRow(yIndex++);
            cell = row.createCell(xIndex);
            cell.setCellStyle(style);
            cell.setCellValue("Abnormal data is empty.");
        }
        return spreadsheet;
    }

    //客製化個人亮燈頻率excel download
    public static void addBABPersonalAlarm(String lineType, String sitefloor, String startDate, String endDate) {
        List<Map> personalAlarms = BasicService.getCountermeasureService().getPersonalAlm(lineType, sitefloor, startDate, endDate);
//        workbook = new HSSFWorkbook();//測試單一的function需要先new 一次，此function在工單列表excel download時是合併在sheet2中的

//        HSSFSheet spreadsheet = workbook.createSheet("test");
        HSSFSheet spreadsheet = createExcelSheet();
        HSSFRow row = spreadsheet.createRow(yIndex);
        HSSFCell cell;
        HSSFCellStyle style = workbook.createCellStyle();
        Map firstData = maxMapInList(personalAlarms);
        Iterator it = firstData.keySet().iterator();
        while (it.hasNext()) {
            if (xIndex == 7) {
                cell = row.createCell(xIndex++);
                cell.setCellStyle(style);
                setCellValue(cell, "");
            }
            String key = (String) it.next();
            cell = row.createCell(xIndex++);
            cell.setCellValue(key);
            cell.setCellStyle(style);
        }

        xIndex = 0;
        yIndex++; //跳過head to next line

        for (Map data : personalAlarms) {
            List l = new ArrayList();
            l.add(data);
            setTestData(spreadsheet, style, l);
            spreadsheet.createRow(yIndex);
        }
//        outputExcel(workbook, "test"); //Output when user need.
    }

    private static Map maxMapInList(List<Map> l) {
        int maxKeySize = 0;
        Map map = null;
        for (Map m : l) {
            if (m.size() > maxKeySize) {
                map = m;
                maxKeySize = m.size();
            }
        }
        return map;
    }

    private static HSSFSheet setTestData(HSSFSheet spreadsheet, HSSFCellStyle style, List<Map> l) {
        HSSFRow row;
        HSSFCell cell;
        if (!l.isEmpty()) {
            //Set the header
            Iterator it;
            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(yIndex++);
                while (it.hasNext()) {
                    if (xIndex == 7) {
                        cell = row.createCell(xIndex++);
                        cell.setCellStyle(style);
                        setCellValue(cell, "");
                    }
                    cell = row.createCell(xIndex++);
                    cell.setCellStyle(style);
                    setCellValue(cell, m.get(it.next()));
                }
                xIndex = 0;//Reset the cell index and begin next data line insert.
            }
        } else {
            row = spreadsheet.createRow(yIndex++);
            cell = row.createCell(xIndex);
            cell.setCellStyle(style);
            cell.setCellValue("Abnormal data is empty.");
        }
        return spreadsheet;
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit();
        ExcelGenerator.sensorAbnormalDataGenerate("2016-10-14");
    }
}
