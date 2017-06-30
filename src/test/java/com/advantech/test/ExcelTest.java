/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.XlsWorkBook;
import com.advantech.excel.XlsWorkSheet;
import static com.advantech.helper.HibernateObjectPrinter.print;
import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.UserService;
import com.advantech.service.WorktimeService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.BeforeClass;
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
    "classpath:servlet-context.xml",
    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelTest {

    @Autowired
    private TypeService typeService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private UserService userService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private WorktimeService worktimeService;

    private static XlsWorkBook workbook;
//  XlsWorkSheet sheet;  

    @BeforeClass
    public static void initSheet() throws IOException {
        String fileName = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\sample2.xls";
//        File f = new File(fileName);
//      System.out.println(f.getAbsolutePath());  
//      f.getAbsolutePath();  
        workbook = new XlsWorkBook(fileName);

    }

//    @Test
    public void testSheetNames() {
        assertNotNull(workbook);

        assertNotNull(workbook.getSheet("sheet1"));
    }

//    @Test
    public void testCellValue() throws Exception {
        XlsWorkSheet sheet = workbook.getSheet("sheet1");
        String rowId = sheet.getValue(0, "id").toString();
        String modelName = sheet.getValue(0, "modelName").toString();
        String typeName = sheet.getValue(0, "typeName").toString();

        assertEquals("8278", rowId);
        assertEquals("test1122", modelName);
        assertEquals("MP", typeName);
    }

//    @Test
    public void testBuildBeans() {
        XlsWorkSheet sheet = workbook.getSheet("sheet1");
        List<Worktime> hgList = sheet.buildBeans(Worktime.class);

        assertEquals(7, hgList.size());

        assertEquals("test1122", hgList.get(0).getModelName());
        assertEquals("test3344", hgList.get(1).getModelName());
        assertTrue(new BigDecimal(3).compareTo(hgList.get(0).getProductionWt()) == 0);
        assertTrue(new BigDecimal(1).compareTo(hgList.get(1).getProductionWt()) == 0);

//        for (Worktime w : hgList) {
//            System.out.println(new Gson().toJson(w));
//        }
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testUser() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, User> userOptions = toSelectOptions(userService.findAll());
        User andy = userOptions.get("CYJAndy.Chiu");
        assertTrue(andy != null);
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testInsert() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {
        XlsWorkSheet sheet = workbook.getSheet("sheet2");
        List<Worktime> hgList = sheet.buildBeans(Worktime.class);
        assertEquals(3, hgList.size());
        hgList = retriveRelativeColumns(sheet, hgList);

        for (Worktime w : hgList) {
            worktimeService.insert(w);
            print(w);
        }
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testUpdate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {
        //先將非關聯的欄位填入
        XlsWorkSheet sheet = workbook.getSheet("sheet1");
        List<Worktime> hgList = sheet.buildBeans(Worktime.class);
        assertEquals(7, hgList.size());
        hgList = retriveRelativeColumns(sheet, hgList);

        for (Worktime w : hgList) {
            worktimeService.merge(w);
        }

    }

    @Transactional
    private List retriveRelativeColumns(XlsWorkSheet sheet, List<Worktime> hgList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Map<String, Type> typeOptions = toSelectOptions(typeService.findAll());
        Map<String, Floor> floorOptions = toSelectOptions(floorService.findAll());
        Map<String, User> userOptions = toSelectOptions(userService.findAll());
        Map<String, Flow> flowOptions = toSelectOptions(flowService.findAll());
        Map<String, Pending> pendingOptions = toSelectOptions(pendingService.findAll());
        Map<String, PreAssy> preAssyOptions = toSelectOptions(preAssyService.findAll());

        //設定關聯by name
        for (int i = 0; i < hgList.size(); i++) {
            Worktime w = hgList.get(i);
            w.setType(typeOptions.get(sheet.getValue(i, "typeName").toString()));
            w.setFloor(floorOptions.get(sheet.getValue(i, "floorName").toString()));

            String eeUserName = sheet.getValue(i, "eeOwnerName").toString();
            String speUserName = sheet.getValue(i, "speOwnerName").toString();
            String qcUserName = sheet.getValue(i, "qcOwnerName").toString();

            w.setUserByEeOwnerId(valid(eeUserName, userOptions.get(eeUserName)));
            w.setUserBySpeOwnerId(valid(speUserName, userOptions.get(speUserName)));
            w.setUserByQcOwnerId(valid(qcUserName, userOptions.get(qcUserName)));

            String babFlowName = sheet.getValue(i, "babFlowName").toString();
            String pkgFlowName = sheet.getValue(i, "packingFlowName").toString();
            String testFlowName = sheet.getValue(i, "testFlowName").toString();

            w.setFlowByBabFlowId(valid(babFlowName, flowOptions.get(babFlowName)));
            w.setFlowByPackingFlowId(valid(pkgFlowName, flowOptions.get(pkgFlowName)));
            w.setFlowByTestFlowId(valid(testFlowName, flowOptions.get(testFlowName)));

            w.setPending(pendingOptions.get(sheet.getValue(i, "pendingName").toString()));

            String preAssyName = sheet.getValue(i, "preAssyName").toString();
            w.setPreAssy(valid(preAssyName, preAssyOptions.get(preAssyName)));
        }

        return hgList;

    }

    private <T extends Object> Map<String, T> toSelectOptions(List l) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map m = new HashMap();
        if (!l.isEmpty()) {
            Object firstObj = l.get(0);
            boolean isUserObject = firstObj instanceof User;
            for (Object obj : l) {
                String name = (String) PropertyUtils.getProperty(obj, isUserObject ? "username" : "name");
                m.put(isUserObject ? name : name, obj);
            }
        }
        return m;
    }

    private <T extends Object> T valid(String objName, T obj) throws Exception {
        if (objName != null && !"".equals(objName) && obj == null) {
            throw new Exception("Object name " + objName + " found but object is not exist.");
        } else {
            return obj;
        }
    }
    
   
}
