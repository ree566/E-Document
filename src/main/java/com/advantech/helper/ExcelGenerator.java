/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.Bab;
import com.advantech.service.BabService;
import com.advantech.service.FbnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import static java.lang.System.out;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng
 */
//@Component
public class ExcelGenerator {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

    @Autowired
    private FbnService fbnService;

    @Autowired
    private BabService babService;

    private Workbook workbook;
    private Sheet spreadsheet;
    private int sheetNum = 1;

    private final DatetimeGenerator dg = new DatetimeGenerator("E yyyy/MM/dd HH:mm");

    private int xIndex = 0, yIndex = 0;

    private int baseXIndex = 0, baseYIndex = 0;

    private List<Integer> skipSpecialFormatIndex;
    private List<String> columnHeaderNames;

    private final String emptyMessage = "Data is empty.";

    private CellStyle floatCell, percentCell, dateCell;

    private ObjectMapper oMapper;

    public ExcelGenerator() {
        init();
    }

    private void init() {
        sheetNum = 1;
        indexInit();
        workbook = new HSSFWorkbook();
        floatCell = this.createFloatCell();
        percentCell = this.createPercentCell();
        dateCell = this.createDateCell();
        oMapper = new ObjectMapper();
        log.info("New one workbook");
    }

    private void indexInit() {
        xIndex = baseXIndex;
        yIndex = baseYIndex;
        this.skipSpecialFormatIndex = new ArrayList();
        this.columnHeaderNames = new ArrayList();
    }

    public void createExcelSheet() {
        createExcelSheet("sheet" + (sheetNum++));
    }

    public void createExcelSheet(String sheetName) {
        spreadsheet = workbook.createSheet(sheetName);
        indexInit();
    }

    public void addSkipDecimalFormatIndex(Integer... i) {
        this.skipSpecialFormatIndex.addAll(Arrays.asList(i));
    }

    public void specifyColumnHeaders(String... name) {
        this.columnHeaderNames.addAll(Arrays.asList(name));
    }

    public Workbook generateWorkBooks(List<Map>... data) {
        for (List<Map> l : data) {
            generateWorkBook(l);
        }
        return workbook;
    }

    private Workbook generateWorkBook(List<Map> data) {
        if (spreadsheet == null) {
            createExcelSheet();
        }

        Row row = spreadsheet.createRow(baseXIndex);
        if (!data.isEmpty()) {
            Map firstData = data.get(baseXIndex);
            Iterator it;
            if (this.columnHeaderNames.isEmpty() || columnHeaderNames.size() != firstData.size()) {
                //Set the header
                it = firstData.keySet().iterator();
                while (it.hasNext()) {
                    String columnName = (String) it.next();
                    if (columnName.contains("*")) {
                        this.addSkipDecimalFormatIndex(yIndex);
                    }
                    setCellValue(row.createCell(yIndex++), columnName);
                }
            } else {
                for (String headerName : this.columnHeaderNames) {
                    setCellValue(row.createCell(yIndex++), headerName);
                }
            }

            xIndex++;
            yIndex = baseYIndex;
            for (Map m : data) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(xIndex++);
                while (it.hasNext()) {
                    setCellValue(row.createCell(yIndex++), m.get(it.next()));
                }
                yIndex = baseYIndex;//Reset the cell index and begin next data line insert.
            }
        } else {
            setCellValue(row.createCell(baseYIndex), emptyMessage);
        }
        return workbook;
    }

    private Cell setCellValue(Cell cell, Object value) {
        if (value instanceof Clob) {
            cell.setCellValue(StringParser.clobToString((Clob) value));
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
            cell.setCellStyle(dateCell);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            if (skipSpecialFormatIndex.contains(cell.getColumnIndex())) {
                cell.setCellStyle(floatCell);
            } else {
                cell.setCellStyle(percentCell);
            }
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((Number) value).doubleValue());
            if (skipSpecialFormatIndex.contains(cell.getColumnIndex())) {
                cell.setCellStyle(floatCell);
            } else {
                cell.setCellStyle(percentCell);
            }
        } else if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value.toString());
        }
        return cell;
    }

    private CellStyle createFloatCell() {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.0"));
        return style;
    }

    private CellStyle createPercentCell() {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        return style;
    }

    private CellStyle createDateCell() {
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        short df = createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm");
        dateCellStyle.setDataFormat(df);
        return dateCellStyle;
    }

    private CellStyle createTestCell() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.RED.index);
        style.setFont(font);
        return style;
    }

    public void formatExcel() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            Row row = sheet.getRow(0);
            for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
                sheet.autoSizeColumn(colNum);
            }
        }
    }

    public void outputExcel(Workbook w, String fileName) {
        this.workbook = w;
        this.outputExcel(fileName);
    }

    public void outputExcel(String fileName) {
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        String fileExt = getFileExt(workbook);
        fileName += fileExt;
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filePath + fileName);
            workbook.write(fileOut);
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

    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * <pre>客製化style的excel pattern.</pre>
     * <pre>Generate sensor abnormal data in yesterday with file output.</pre>
     * <pre>File name is a date, and file output path is setting in function outputExcel(workbookObj, fileName).</pre>
     */
    private void generateSensorAbnormalData() {
        //異常Sensor Data generate
        generateSensorAbnormalData(new DatetimeGenerator("yyyy-MM-dd").getYesterday());
    }

    /**
     * Date in yyyy-MM-dd Generate target date sensor abnormal data with file
     * output.
     *
     * @param date
     */
    private void generateSensorAbnormalData(String date) {
        DateTime d = new DateTime(date);
        List<Bab> babs = babService.findByDate(d, d);
        workbook = new HSSFWorkbook();

        spreadsheet = workbook.createSheet("test");

        int dataCount = 0;

        for (Bab bab : babs) {
            int bab_id = bab.getId();
            List<Map> abnormalDataTotal = fbnService.getTotalAbnormalData(bab_id);
            List<Map> abnormalData = fbnService.getAbnormalData(bab_id);

            //Make sure the data if empty or not(deadLock always happen).
            if (abnormalDataTotal.isEmpty()) {
                abnormalDataTotal = fbnService.getTotalAbnormalData(bab_id);
            }

            if (abnormalData.isEmpty()) {
                abnormalData = fbnService.getAbnormalData(bab_id);
            }

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(dataCount++ % 2 == 0 ? HSSFColor.LIGHT_TURQUOISE.index : HSSFColor.LIGHT_GREEN.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            setData(spreadsheet, style, oMapper.convertValue(bab, Map.class));
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalDataTotal);
            spreadsheet.createRow(++yIndex);

            setData(spreadsheet, style, abnormalData);
            spreadsheet.createRow(yIndex += 2);
        }
        outputExcel(date);
    }

    private Sheet setData(Sheet spreadsheet, CellStyle style, Map m) {
        List l = new ArrayList();
        l.add(m);
        return setData(spreadsheet, style, l);
    }

    private Sheet setData(Sheet spreadsheet, CellStyle style, List<Map> l) {
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

            xIndex = baseXIndex;
            yIndex++;

            for (Map m : l) {
                it = m.keySet().iterator();
                row = spreadsheet.createRow(yIndex++);
                while (it.hasNext()) {
                    cell = row.createCell(xIndex++);
                    cell.setCellStyle(style);
                    setCellValue(cell, m.get(it.next()));
                }
                xIndex = baseXIndex;//Reset the cell index and begin next data line insert.
            }
        } else {
            row = spreadsheet.createRow(yIndex++);
            cell = row.createCell(xIndex);
            cell.setCellStyle(style);
            cell.setCellValue(emptyMessage);
        }
        return spreadsheet;
    }

    //客製化個人亮燈頻率excel download
    public void appendSpecialPattern(
            List<Map> list, int colSeparateColIndex,
            String failPersonNumLetter,
            String failPersonColumnName,
            String failTotalNumLetter,
            String failTotalColumnName) {
        if (spreadsheet == null) {
            createExcelSheet();
        }
        Row row = spreadsheet.createRow(yIndex);
        Cell cell;
        if (!list.isEmpty()) {
            Map firstData = maxMapInList(list);
            int maxDataIndex;
            List<String> idCols = new ArrayList();
            List<String> failPercentCols = new ArrayList();
            Iterator it = firstData.keySet().iterator();

            while (it.hasNext()) {
                if (xIndex == colSeparateColIndex) {
                    setCellValue(row.createCell(xIndex++), "");
                }
                cell = row.createCell(xIndex++);
                setCellValue(cell, it.next());

                if (xIndex > colSeparateColIndex) {
                    String colNumLetter = CellReference.convertNumToColString(cell.getColumnIndex());
                    if ((xIndex - (colSeparateColIndex + 1)) % 2 == 0) {
                        failPercentCols.add(colNumLetter);
                    } else {
                        idCols.add(colNumLetter);
                    }
                }
            }

            System.out.println(failPercentCols.toString());
            System.out.println(idCols.toString());

            maxDataIndex = xIndex;
            xIndex = baseXIndex;//跳回第一行
            yIndex++; //跳過head to next line

            for (Map data : list) {
                it = data.keySet().iterator();
                row = spreadsheet.createRow(yIndex++);
                while (it.hasNext()) {
                    if (xIndex == colSeparateColIndex) {
                        setCellValue(row.createCell(xIndex++), "");
                    }
                    setCellValue(row.createCell(xIndex++), data.get(it.next()));
                }
                xIndex = baseXIndex;//Reset the cell index and begin next data line insert.
                spreadsheet.createRow(yIndex);
            }

            //依照給定的column name取得index
            int failPersonNumIndex = CellReference.convertColStringToIndex(failPersonNumLetter);
            int failTotalNumIndex = CellReference.convertColStringToIndex(failTotalNumLetter);

            //Set the final two formula column.
            spreadsheet.getRow(baseXIndex).createCell(failPersonNumIndex).setCellValue(failPersonColumnName);
            spreadsheet.getRow(baseXIndex).createCell(failTotalNumIndex).setCellValue(failTotalColumnName);

            //set unused column to hidden
            for (int a = maxDataIndex; a < failPersonNumIndex - 1; a++) {
                spreadsheet.setColumnHidden(a, true);
            }

            //set final two column formula at Num Z and Num AA
            for (int i = 1; i <= list.size(); i++) {
                Row maxium = spreadsheet.getRow(i);
                int currentYIndex = i + 1;

                //瓶頸站人名
                cell = maxium.createCell(failPersonNumIndex);
                String formulaString = "";
                String formulaStringEnding = "";
                for (int j = 0, m = idCols.size(); j < m; j++) {
                    String formulaCol = failTotalNumLetter + currentYIndex;
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
                cell = maxium.createCell(failTotalNumIndex);
                String formulaString1 = "MAX(";
                for (int j = 0; j < failPercentCols.size(); j++) {
                    formulaString1 += failPercentCols.get(j) + currentYIndex + ",";
                }
                formulaString1 += ")";
                cell.setCellFormula(formulaString1);
                cell.setCellStyle(percentCell);
            }
        } else {
            setCellValue(row.createCell(baseYIndex), emptyMessage);
        }
    }

    private Map maxMapInList(List<Map> l) {
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

}
