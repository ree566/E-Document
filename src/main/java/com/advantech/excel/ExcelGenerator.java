/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.excel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
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

    private Workbook workbook;
    private Sheet spreadsheet;
    private int sheetNum = 1;

    private int xIndex = 0, yIndex = 0;

    private int baseXIndex = 0, baseYIndex = 0;

    private List<Integer> skipSpecialFormatIndex;
    private List<String> columnHeaderNames;

    private final String emptyMessage = "Data is empty.";

    private CellStyle floatCell, percentCell, dateCell;

    private ObjectMapper m;

    private final String dateFormat = "yyyy-MM-dd HH:mm";
    private SimpleDateFormat sdf;

    public ExcelGenerator() {
        workbook = new HSSFWorkbook();
        init();
    }

    public ExcelGenerator(Workbook workbook) {
        this.workbook = workbook;
        init();
    }

    private void init() {
        sheetNum = 1;
        indexInit();
        floatCell = this.createFloatCell();
        percentCell = this.createPercentCell();
        dateCell = this.createDateCell();
        m = new ObjectMapper();
        sdf = new SimpleDateFormat(dateFormat);
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

    public Workbook generateWorkBooks(List... data) throws JsonProcessingException, IOException {
        for (List<Object> l : data) {
            generateWorkBook(l);
        }
        return workbook;
    }

    private Workbook generateWorkBook(List data) throws JsonProcessingException, IOException {

        if (spreadsheet == null) {
            createExcelSheet();
        }

        Row row = spreadsheet.createRow(baseXIndex);
        if (!data.isEmpty()) {

            Map firstData = m.convertValue(data.get(baseXIndex), Map.class);
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
            for (Object obj : data) {
                Map map = m.convertValue(obj, Map.class);
                it = map.keySet().iterator();
                row = spreadsheet.createRow(xIndex++);
                while (it.hasNext()) {
                    setCellValue(row.createCell(yIndex++), map.get(it.next()));
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
            cell.setCellValue(clobToString((Clob) value));
        } else if (value instanceof java.util.Date) {
            cell.setCellValue(sdf.format((java.util.Date) value));
            cell.setCellStyle(dateCell);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(floatCell);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((Number) value).doubleValue());
            cell.setCellStyle(floatCell);
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
        short df = createHelper.createDataFormat().getFormat(dateFormat);
        dateCellStyle.setDataFormat(df);
        return dateCellStyle;
    }

    private CellStyle createTestCell() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
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
    
    public static String clobToString(Clob data) {
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
}
