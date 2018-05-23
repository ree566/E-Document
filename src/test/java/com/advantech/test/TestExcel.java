/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.model.FqcModelStandardTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

    @Test
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
                    Integer avg = Integer.parseInt(((String)getCellValue(row, "C")).trim().replace("秒", ""));
                    String modelB = getCellValue(row, "B") == null ? "無生產" : 
                            ((String) getCellValue(row, "B")).trim();

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

}
