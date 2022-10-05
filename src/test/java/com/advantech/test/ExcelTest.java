/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.WorktimeJxlsModel;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Cobot;
import com.advantech.model.Type;
import com.advantech.model.Worktime;
import com.advantech.service.UserService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.ModelResponsorUploadPort;
import com.advantech.webservice.port.SopUploadPort;
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
import javax.transaction.Transactional;
import static org.junit.Assert.*;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.Rollback;
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

//    private final String xmlConfig = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime.xml";
//    private final String dataXLS = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime-template.xls";
    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ResourceLoader resourceLoader;

//    @Test
    public void testJxls() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\worktime-template.xls");
        String filePath = "C:\\Users\\Wei.Cheng\\Desktop\\worktime_output.xls";

        try ( InputStream inputXML = r.getInputStream();  InputStream inputXLS = new FileInputStream(filePath);) {

            assertNotNull(inputXML);

            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

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

    @Autowired
    private SopUploadPort sopPort;

    @Autowired
    private ModelResponsorUploadPort mappingUserPort;

    @Test
    @Transactional
    @Rollback(false)
    public void testFileSyncToDb() throws Exception {

        String syncFilePath = "C:\\Users\\wei.cheng\\Desktop\\複本 ADAM 調整0.3分鐘的機種.xlsx";
        try ( InputStream is = new FileInputStream(new File(syncFilePath))) {

            Session session = sessionFactory.getCurrentSession();
            List<Worktime> l = worktimeService.findAll();

            Workbook workbook = WorkbookFactory.create(is);

//            List<User> users = userService.findByUnitName("MPM");
            Cobot cobot = session.get(Cobot.class, 2);

            for (int sheetPage = 0; sheetPage <= 0; sheetPage++) {
                Sheet sheet = workbook.getSheetAt(sheetPage);
                for (int i = 1, maxNumberfRows = sheet.getPhysicalNumberOfRows(); i < maxNumberfRows; i++) {
                    Row row = sheet.getRow(i); // 取得第 i Row
                    if (row != null) {

                        //Because cell_A will auto convert to number if modelName only contains numbers.
                        //Search will cause exception when not add convert lines
                        Cell model_cell = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                        model_cell.setCellType(CellType.STRING);

                        String modelName = ((String) getCellValue(row, "A")).trim();

                        Worktime w = l.stream().filter(o -> Objects.equals(o.getModelName(), modelName)).findFirst().orElse(null);
                        if (w == null) {
                            System.out.println("\tCan't find modelName: " + modelName + " in worktime");
                            continue;
                        }

                        w = worktimeService.setCobotWorktime(w);
//                        String userName = ((String) getCellValue(row, "D")).trim();
//                        User user = users.stream().filter(u -> u.getUsername().equals(userName)).findFirst().orElse(null);
//                        w.setUserByMpmOwnerId(user);
                        session.merge(w);
//                        mappingUserPort.update(w);
                        System.out.println("Update modelName: " + modelName);
                    }
                }
            }
        }
    }

    private Object getCellValue(Row row, String letter) {
        Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex(letter));
        CellType cellType = cell.getCellType();
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

    private BigDecimal objToBigDecimal(Object o) {
        return o != null && NumberUtils.isNumber(o.toString()) ? new BigDecimal(o.toString()) : null;
    }

//    @Test
    public void testJxlsReader() throws Exception {
        Resource r = resourceLoader.getResource("classpath:worktime.xml");
        String filePath = "C:\\Users\\Wei.Cheng\\Desktop\\worktime-template.xls";

        try ( InputStream inputXML = r.getInputStream();  InputStream inputXLS = new FileInputStream(filePath);) {
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
//            ReaderConfig.getInstance().setSkipErrors(true);

            List<WorktimeJxlsModel> worktimes = new ArrayList();

            Map beans = new HashMap();
            beans.put("worktimes", worktimes);
            mainReader.read(inputXLS, beans);

            assertEquals(2, worktimes.size());

            HibernateObjectPrinter.print(worktimes);
        }
    }
}
