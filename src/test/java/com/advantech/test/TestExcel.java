/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.ExampleEventUserModel;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.model.Floor;
import com.advantech.model.FqcModelStandardTime;
import com.advantech.model.LineType;
import com.advantech.model.PrepareSchedule;
import com.monitorjbl.xlsx.StreamingReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestExcel {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private SessionFactory sessionFactory;

//    @Test
    public void testAddDataToTemp() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\report_1_template.xls");
        try (InputStream is = r.getInputStream()) {

            List l = new ArrayList();

            try (OutputStream os = new FileOutputStream("C:\\Users\\Wei.Cheng\\Desktop\\object_collection_output.xls")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Context context = new Context();
                context.putVar("sheetViews", l);
                context.putVar("dateFormat", dateFormat);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
                evaluator.getJexlEngine().setSilent(false);

                JxlsHelper helper = JxlsHelper.getInstance();
                helper.processTemplate(context, transformer);
//                .processTemplate(is, os, context);
            }
        }
    }

//    @Test
//    @Transactional
//    @Rollback(true)
    public void getData() {
        String testModel = "UTC-520FP-IKA0E";
        String lineTypeName = "ASSY";

        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(Bab.class, "b");
        List<Bab> l = c.createAlias("b.line", "l")
                .createAlias("l.lineType", "lt")
                .add(Restrictions.eq("b.modelName", testModel))
                .add(Restrictions.eq("lt.name", lineTypeName))
                .add(Restrictions.eq("b.babStatus", BabStatus.CLOSED))
                .setMaxResults(10)
                .list();

        l.forEach(b -> {
            Hibernate.initialize(b.getBabPcsDetailHistorys());
            Hibernate.initialize(b.getBabAlarmHistorys());
        });

        HibernateObjectPrinter.print(l);

//        return l;
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testReadExcel() throws FileNotFoundException, IOException, InvalidFormatException {
        String syncFilePath = "C:\\Users\\Wei.Cheng\\Desktop\\複本 FQC機種標準工時管控表.xlsx";
        try (InputStream is = new FileInputStream(new File(syncFilePath))) {

            Session session = sessionFactory.getCurrentSession();

            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1, maxNumberfRows = sheet.getPhysicalNumberOfRows(); i < maxNumberfRows; i++) {
                Row row = sheet.getRow(i); // 取得第 i Row
                if (row != null) {

                    //Because cell_A will auto convert to number if modelName only contains numbers.
                    //Search will cause exception when not add convert lines
                    Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                    cell_A.setCellType(CellType.STRING);

                    String modelNameC = ((String) getCellValue(row, "A")).trim();
                    Integer avg = Integer.parseInt(((String) getCellValue(row, "C")).trim().replace("秒", ""));
                    String modelB = getCellValue(row, "B") == null ? "無生產"
                            : ((String) getCellValue(row, "B")).trim();

                    if (!"無生產".equals(modelB)) {
                        FqcModelStandardTime standardTime = new FqcModelStandardTime();
                        standardTime.setModelNameCategory(modelNameC);
                        standardTime.setStandardTime(avg);
                        session.save(standardTime);
                    }

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
                        case NUMERIC:
                            return cell.getNumericCellValue();
                        case STRING:
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

//    @Test
    @Transactional
    @Rollback(false)
    public void testReadExcel2() throws Exception {
        int[] floors = {5, 6};

        for (int floor : floors) {

            String syncFilePath = "C:\\Users\\wei.cheng\\Desktop\\excel_test\\APS " + floor + "F 組裝排程.xlsx";

            Session session = sessionFactory.getCurrentSession();
            LineType assy = session.get(LineType.class, 1);
            Floor f = session.get(Floor.class, floor == 5 ? 1 : 2);

            try (Workbook workbook = WorkbookFactory.create(new File(syncFilePath), "234", true)) {
                Sheet sheet = workbook.getSheet(floor + "F--前置&組裝");

                DateTime d = new DateTime().withTime(0, 0, 0, 0);

                int dateIdx = 5;
                int titleIdx = 6;

                int patchColumn = findColumnIdx(sheet.getRow(dateIdx), d);
                int lineTypeIdx = findColumnIdx(sheet.getRow(titleIdx), "料號&製程段");
                int modelNameIdx = findColumnIdx(sheet.getRow(titleIdx), "料號");
                int poIdx = findColumnIdx(sheet.getRow(titleIdx), "工單");
                int totalQtyIdx = findColumnIdx(sheet.getRow(titleIdx), "工單數");
                int scheduleQtyIdx = patchColumn;
                int timeCostIdx = patchColumn + 1;

                //Step 1: First get column index matches the current date
                //Step 2: Filter data B7 contain word "BASSY" & 排產量 is not blank
                //Iterate through each rows one by one
//            List<PrepareSchedule> prepareSchedules = new ArrayList();
                int cnt = 0;

                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    try {
                        //Skip row to main data
                        if (cnt > titleIdx) {
                            Row row = rowIterator.next();
                            Cell cell_LineType = row.getCell(lineTypeIdx);
                            Cell cell_OutputCnt = row.getCell(patchColumn);
                            if (cell_LineType != null && cell_OutputCnt != null) {
                                cell_LineType.setCellType(CellType.STRING);
                                if (cell_LineType.getRichStringCellValue().toString().contains("BASSY") && cell_OutputCnt.getCellType() != CellType.BLANK) {

                                    Cell cell_ModelName = row.getCell(modelNameIdx);
                                    Cell cell_Po = row.getCell(poIdx);
                                    Cell cell_TotalQty = row.getCell(totalQtyIdx);
                                    Cell cell_ScheduleQty = row.getCell(scheduleQtyIdx);
                                    Cell cell_TimeCost = row.getCell(timeCostIdx);

                                    System.out.println(cell_ModelName.getStringCellValue());

                                    PrepareSchedule p = new PrepareSchedule();
                                    p.setModelName(cell_ModelName.getStringCellValue());
                                    p.setPo(cell_Po.getStringCellValue());
                                    p.setTotalQty((int) cell_TotalQty.getNumericCellValue());
                                    p.setScheduleQty((int) cell_ScheduleQty.getNumericCellValue());
                                    p.setTimeCost(new BigDecimal(cell_TimeCost.getNumericCellValue()));
                                    p.setLineType(assy);
                                    p.setOnBoardDate(d.toDate());
                                    p.setFloor(f);
//                            prepareSchedules.add(p);
//                                    session.save(p);
                                }
                            }
                        }
                        cnt++;
                    } catch (Exception e) {
                        System.out.println(cnt);
                        System.out.println(e);
                    }
                }
            }
        }
    }

    private int findColumnIdx(Row r, Object keyword) throws Exception {

        int patchColumn = -1;

        for (int cn = 0; cn < r.getLastCellNum(); cn++) {
            Cell c = r.getCell(cn);
            if (c == null || c.getCellType() == CellType.BLANK) {
                // Can't be this cell - it's empty
                continue;
            }
            if (c.getCellType() == CellType.NUMERIC) {
                if (keyword != null && keyword instanceof DateTime && HSSFDateUtil.isCellDateFormatted(c)) {
                    DateTime v = new DateTime(c.getDateCellValue());
                    if (((DateTime) keyword).isEqual(v)) {
                        patchColumn = cn;
                        break;
                    }
                }
            } else if (c.getCellType() == CellType.STRING) {
                if (Objects.equals(keyword, c.getStringCellValue())) {
                    patchColumn = cn;
                    break;
                }
            }
        }

        if (patchColumn == -1) {
            throw new Exception("None of the cells in the first row were Patch");
        }

        return patchColumn;
    }

    @Test
    public void testStreamReader() throws Exception {
        InputStream is = new FileInputStream(new File("C:\\Users\\wei.cheng\\Desktop\\excel_test\\APS 5F 組裝排程.xlsx"));
        Workbook workbook = StreamingReader.builder()
                .password("234")
                .rowCacheSize(100) // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096) // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)

        Sheet sheet = workbook.getSheet(5 + "F--前置&組裝");
        
//        for (Sheet sheet : workbook) {
//            System.out.println(sheet.getSheetName());
            for (Row r : sheet) {
                for (Cell c : r) {
                    System.out.println(c.getStringCellValue());
                }
            }
//        }
    }
    
//    @Test
    public void testExampleEventUserModel() throws Exception {
        ExampleEventUserModel example = new ExampleEventUserModel();

        String filePath = "‪C:\\Users\\wei.cheng\\Desktop\\excel_test\\活頁簿1.xlsx";

//        example.processOneSheet(filePath);
//        example.processAllSheets(filePath);
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        workbook.close();
    }
}
