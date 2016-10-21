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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ExcelGenerator {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

    private static Workbook workbook;
    private static int sheetNum = 1;

    private static final DatetimeGenerator dg = new DatetimeGenerator("E yyyy/MM/dd HH:mm");

    private static final int CUSTOM_EXCEL_SEPARATE_COLINDEX = 9;
    //Set values
    private static int xIndex = 0, yIndex = 0;

    private static void init() {
        sheetNum = 1;
        xIndex = 0;
        yIndex = 0;
        workbook = new HSSFWorkbook();
        log.info("New one workbook");
    }

    private static Sheet createExcelSheet() {
        return workbook.createSheet("sheet" + (sheetNum++));
    }

    public static Workbook generateWorkBooks(List<Map>... data) {
        init();
        for (List<Map> l : data) {
            generateWorkBook(l);
        }
        return workbook;
    }

    private static Workbook generateWorkBook(List<Map> data) {
        Sheet spreadsheet = createExcelSheet();
        Row row = spreadsheet.createRow(0);
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

    private static Cell setCellValue(Cell cell, Object value) {
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

    private static Cell createFloatCell(Cell cell) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        cell.setCellStyle(style);
        return cell;
    }

    public static void formatExcel() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            Row row = sheet.getRow(0);
            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                sheet.autoSizeColumn(colNum);
            }
        }
    }

    public static void outputExcel(Workbook w, String fileName) {
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        String fileExt = getFileExt(w);
        fileName += fileExt;

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

    public static String getFileExt(Workbook w) {
//        if (w instanceof HSSFWorkbook) {
//            return ".xls";
//        } else if (w instanceof SXSSFWorkbook || w instanceof XSSFWorkbook) {
//            return ".xlsx";
//        } else {
//            return null;
//        }
        //取消註解if system add xlsx support
        return ".xls";
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

        Sheet spreadsheet = workbook.createSheet("test");

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

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(dataCount++ % 2 == 0 ? HSSFColor.LIGHT_TURQUOISE.index : HSSFColor.LIGHT_GREEN.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            setData(spreadsheet, style, bab);
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalDataTotal);
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalData);
            spreadsheet.createRow(yIndex += 2);
        }
        outputExcel(workbook, date);
    }

    private static Sheet setData(Sheet spreadsheet, CellStyle style, Map m) {
        List l = new ArrayList();
        l.add(m);
        return setData(spreadsheet, style, l);
    }

    private static Sheet setData(Sheet spreadsheet, CellStyle style, List<Map> l) {
        Row row = spreadsheet.createRow(yIndex);
        Cell cell;
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

    public static void addBABPersonalAlarm(String lineType, String sitefloor, String startDate, String endDate) {
        List personalAlarms = BasicService.getCountermeasureService().getPersonAlarm(lineType, sitefloor, startDate, endDate);
        addBABPersonalAlarm(personalAlarms); 
    }

    //客製化個人亮燈頻率excel download
    public static void addBABPersonalAlarm(List<Map> personalAlarms) {
//        workbook = new XSSFWorkbook();//測試單一的function需要先new 一次，此function在工單列表excel download時是合併在sheet2中的

        Sheet spreadsheet = createExcelSheet();
        Row row = spreadsheet.createRow(yIndex);
        Cell cell;
        CellStyle style = workbook.createCellStyle();
        Map firstData = maxMapInList(personalAlarms);
        int maxDataIndex;
        List<String> idCols = new ArrayList();
        List<String> failPercentCols = new ArrayList();

        Iterator it = firstData.keySet().iterator();
        while (it.hasNext()) {
            if (xIndex == CUSTOM_EXCEL_SEPARATE_COLINDEX) {
                cell = row.createCell(xIndex++);
                cell.setCellStyle(style);
                setCellValue(cell, "");
            }
            String key = (String) it.next();
            cell = row.createCell(xIndex++);
            cell.setCellValue(key);
            cell.setCellStyle(style);

            if (xIndex > CUSTOM_EXCEL_SEPARATE_COLINDEX) {
                String colNumLetter = CellReference.convertNumToColString(cell.getColumnIndex());
                if ((xIndex - (CUSTOM_EXCEL_SEPARATE_COLINDEX + 1)) % 2 == 0) {
                    failPercentCols.add(colNumLetter);
                } else {
                    idCols.add(colNumLetter);
                }
            }
        }

        maxDataIndex = xIndex;
        xIndex = 0;//跳回第一行
        yIndex++; //跳過head to next line

        for (Map data : personalAlarms) {
            List l = new ArrayList();
            l.add(data);
            setTestData(spreadsheet, style, l);
            spreadsheet.createRow(yIndex);
        }

        //設定最後兩攔formula
        String failPersonNumLetter = "Z";
        String failPercentNumLetter = "AA";

        int targetIndex1 = CellReference.convertColStringToIndex(failPersonNumLetter);
        int targetIndex2 = CellReference.convertColStringToIndex(failPercentNumLetter);

        //Set the final two formula column.
        spreadsheet.getRow(0).createCell(targetIndex1).setCellValue("瓶頸站");
        spreadsheet.getRow(0).createCell(targetIndex2).setCellValue("亮燈頻率");

        //set unused column to hidden
        for (int a = maxDataIndex; a < targetIndex1 - 1; a++) {
            spreadsheet.setColumnHidden(a, true);
        }

        for (int i = 1; i <= personalAlarms.size(); i++) {
            Row maxium = spreadsheet.getRow(i);
            int currentYIndex = i + 1;

            //瓶頸站人名
            cell = maxium.createCell(targetIndex1);
            String formulaString = "";
            String formulaStringEnding = "";
            for (int j = 0, m = idCols.size(); j < m; j++) {
                String formulaCol = failPercentNumLetter + currentYIndex;
                String failPercentCol = failPercentCols.get(j) + currentYIndex;
                String userNameCol = idCols.get(j) + currentYIndex;
                formulaString += "if(" + failPercentCol + "=" + formulaCol + "," + userNameCol;
                if (j < m - 1) {
                    formulaString += ",";
                }
                formulaStringEnding += ")";
            }
            formulaString += formulaStringEnding;
            cell.setCellFormula(formulaString);

            //瓶頸站趴數
            cell = maxium.createCell(targetIndex2);
            String formulaString1 = "MAX(";
            for (int j = 0; j < failPercentCols.size(); j++) {
                formulaString1 += failPercentCols.get(j) + currentYIndex + ",";
            }
            formulaString1 += ")";
            cell.setCellFormula(formulaString1);
            createFloatCell(cell);
        }
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

    private static Sheet setTestData(Sheet spreadsheet, CellStyle style, List<Map> l) {
        Row row;
        Cell cell;
        if (!l.isEmpty()) {
            //Set the header
            Iterator it;
            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(yIndex++);
                while (it.hasNext()) {
                    if (xIndex == CUSTOM_EXCEL_SEPARATE_COLINDEX) {
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
        ExcelGenerator.sensorAbnormalDataGenerate();
//        ExcelGenerator.addBABPersonalAlarm("ASSY", "5", "16/10/01", "16/10/17");
//        ExcelGenerator.outputExcel(workbook, "TEST");
    }
}
