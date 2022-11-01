/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static com.advantech.excel.ExcelUtils.getCellValue;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.FlowRuleQueryPort;
import com.advantech.webservice.port.MaterialFlowQueryPort;
import com.advantech.webservice.port.MaterialPropertyQueryPort;
import com.advantech.webservice.port.MaterialPropertyValueQueryPort;
import com.advantech.webservice.port.MaterialPropertyUserPermissionQueryPort;
import com.advantech.webservice.port.MesUserInfoQueryPort;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.port.SopQueryPort;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import com.advantech.webservice.unmarshallclass.MaterialProperty;
import com.advantech.webservice.unmarshallclass.MaterialPropertyUserPermission;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import static junit.framework.Assert.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
//    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class QueryPortTest {

    @Autowired
    private SopQueryPort sopQueryPort;

    @Autowired
    private ModelResponsorQueryPort mrQueryPort;

    @Autowired
    private MesUserInfoQueryPort mesUserQueryPort;

    @Autowired
    private FlowRuleQueryPort flowRuleQueryPort;

    @Autowired
    private MaterialFlowQueryPort materialFlowQueryPort;

    @Autowired
    private MaterialPropertyUserPermissionQueryPort materialPropertyUserPermissionQueryPort;

    @Autowired
    private MaterialPropertyQueryPort materialPropertyQueryPort;

    @Autowired
    private MaterialPropertyValueQueryPort materialPropertyValueQueryPort;

    @Autowired
    private WorktimeService worktimeService;

    private Worktime w;

    @Before
    public void init() {
        w = worktimeService.findByPrimaryKey(897);
    }

//    @Test
    public void testSopQueryPort() throws Exception {
        sopQueryPort.setTypes("T1");
        List l = sopQueryPort.query(w);
        assertEquals(1, l.size());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testModelResponsorQueryPort() throws Exception {
        List l = mrQueryPort.query(w);
        assertEquals(3, l.size());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testMesUserInfoQueryPort() throws Exception {
        List l = mesUserQueryPort.query(w);
        assertEquals(3, l.size());
        HibernateObjectPrinter.print(l);

//        Map m = mesUserQueryPort.transformData(w);
//        HibernateObjectPrinter.print(m);
    }

//    @Test
    public void testFlowRuleQueryPort() throws Exception {
        FlowRule rule = flowRuleQueryPort.query("B", "empty");
        assertNull(rule);
        HibernateObjectPrinter.print(rule);
    }

//    @Test
    public void testMateriaFlowQueryPort() throws Exception {
        Map m = materialFlowQueryPort.transformData(w);
        assertEquals(4, m.size());
        HibernateObjectPrinter.print(m);

        List<MaterialFlow> l = materialFlowQueryPort.query(w);
        assertEquals(4, l.size());
        HibernateObjectPrinter.print(l);

        assertEquals(34200, l.get(0).getId());
        assertEquals(14360, l.get(0).getItemId());
        assertEquals(719, l.get(0).getFlowRuleId());
    }

//    @Test
    public void testMaterialPropertyUserPermissionQueryPort() throws Exception {
        List<MaterialPropertyUserPermission> l = materialPropertyUserPermissionQueryPort.query("A-7060");
        assertEquals(4, l.size());
        assertEquals("BD", l.get(0).getMaterialPropertyNo());
    }

//    @Test
    public void testMaterialPropertyQueryPort() throws Exception {
        List<MaterialProperty> l = materialPropertyQueryPort.query("FC");
        assertEquals(1, l.size());
        assertEquals("FC", l.get(0).getMatPropertyNo());
        System.out.println(l.get(0).getAffPropertyType());
    }

//    @Test
    public void testMaterialPropertValueyQueryPort() throws Exception {
        Worktime w1 = worktimeService.findByModel("2063002307");
        assertNotNull(w1);
        List<MaterialPropertyValue> l = materialPropertyValueQueryPort.query(w1);
        assertEquals(6, l.size());
        MaterialPropertyValue value = l.stream()
                .filter(v -> "KB".equals(v.getMatPropertyNo())).findFirst().orElse(null);
        assertNotNull(value);
        assertEquals(null, value.getAffPropertyValue());
        assertEquals("N", value.getValue());
    }

    @Test
    public void testFlowMappingOnMes() throws FileNotFoundException, IOException, Exception {

        String inputLocation = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\m6_babFlow.xls";
        String outputFile = "C:\\Users\\Wei.Cheng\\Desktop\\m6_babFlow_mod.xls";

        try (InputStream is = new FileInputStream(inputLocation); OutputStream os = new FileOutputStream(outputFile)) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
            style.setFont(font);

            Worktime tempW = new Worktime();

            for (int i = 1, maxNumberfRows = sheet.getPhysicalNumberOfRows(); i < maxNumberfRows; i++) {
                Row row = sheet.getRow(i); // 取得第 i Row
                if (row != null) {

                    //Because cell_A will auto convert to number if modelName only contains numbers.
                    //Search will cause exception when not add convert lines
                    Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                    cell_A.setCellType(CellType.STRING);

                    if (getCellValue(row, "A") == null) {
                        continue;
                    }

                    String modelName = ((String) getCellValue(row, "A")).trim();
                    System.out.println(modelName);
                    tempW.setModelName(modelName);

                    String babFlowName = (String) getCellValue(row, "B");

                    List<MaterialFlow> l = materialFlowQueryPort.query(tempW);

                    MaterialFlow mesBabFlow = l.stream()
                            .filter(f -> "B".equals(f.getUnitNo()))
                            .findAny()
                            .orElse(null);

                    String flowName = mesBabFlow == null ? null : mesBabFlow.getFlowRuleName();

                    Cell cell_C = CellUtil.createCell(row, CellReference.convertColStringToIndex("C"), flowName);
                    cell_C.setCellValue(flowName);
                    if (!Objects.equals(babFlowName, flowName)) {
                        cell_C.setCellStyle(style);
                    }

                }
            }
            workbook.write(os);
            workbook.close();

        }
    }

}
