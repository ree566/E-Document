/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Type;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import static junit.framework.Assert.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.junit.runner.RunWith;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelTest {

    private final String xmlConfig = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime.xml";
    private final String dataXLS = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime-template.xls";

    @Autowired
    private WorktimeService worktimeService;

//    @Test
    public void testJxls() throws Exception {
        try (InputStream inputXML = new FileInputStream(new File(xmlConfig))) {
            assertNotNull(inputXML);

            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            try (InputStream inputXLS = new FileInputStream(new File(dataXLS))) {
                assertNotNull(inputXLS);

                List<Worktime> l = new ArrayList();
                Map beans = new HashMap();
                beans.put("worktimes", l);
                beans.put("type", new Type());
                XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
                assertTrue(readStatus.isStatusOK());
                System.out.println(new Gson().toJson(l));
            }
        }
    }

    public void testFileSyncToDb() throws Exception {
        List<Worktime> l = worktimeService.findAll();
        String syncFilePath = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\conflict_1227.xls";
        try (InputStream is = new FileInputStream(new File(syncFilePath))) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1, maxNumberfRows = sheet.getPhysicalNumberOfRows(); i < maxNumberfRows; i++) {
                Row row = sheet.getRow(i); // 取得第 i Row
                if (row != null) {

                    //Because cell_A will auto convert to number if modelName only contains numbers.
                    //Search will cause exception when not add convert lines
                    Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                    cell_A.setCellType(CellType.STRING);

                    String modelName = (String) getCellValue(row, "A");

                    Worktime w = l.stream().filter(o -> Objects.equals(o.getModelName(), modelName)).findFirst().orElse(null);
                    assertNotNull(w);

                    w.setNsInOneCollectionBox(objToBigDecimal(getCellValue(row, "C")));
                    w.setBiTemperature(objToBigDecimal(getCellValue(row, "E")));
                    w.setBiTime(objToBigDecimal(getCellValue(row, "G")));

//                    Object burnIn = getCellValue(row, "I");
//                    w.setBurnIn(burnIn == null ? "N" : );
                    BigDecimal kA = objToBigDecimal(getCellValue(row, "K"));
                    w.setKeypartA(kA == null ? null : kA.intValue());
                }
            }
        }
    }

    private Object getCellValue(Row row, String letter) {
        Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex(letter));
        CellType cellType = cell.getCellTypeEnum();
        if (null == cellType) {
            return null;
        } else {
            switch (cellType) {
                case STRING:
                    String value = cell.getStringCellValue();
                    return value == null || "".equals(value.trim()) ? null : value;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            return cell.getNumericCellValue();
                        case Cell.CELL_TYPE_STRING:
                            return null;
                    }
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return cell.getNumericCellValue();
                    }
                case BLANK:
                    return null;
                case BOOLEAN:
                    return Boolean.toString(cell.getBooleanCellValue());
                default:
                    return null;
            }
        }
    }

    private BigDecimal objToBigDecimal(Object o) {
        return o != null && NumberUtils.isNumber(o.toString()) ? new BigDecimal(o.toString()) : null;
    }
}
