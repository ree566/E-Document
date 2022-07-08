/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.advantech.service;

import com.advantech.model.Worktime;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
})
@RunWith(SpringJUnit4ClassRunner.class)
public class WorktimeServiceTest {
    
    @Autowired
    WorktimeService instance;
    
    public WorktimeServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findAll method, of class WorktimeService.
     */
    @Test
    public void testFindAll_0args() {
        System.out.println("findAll");
        List<Worktime> result = instance.findAll();
        assertTrue(!result.isEmpty());
    }
//
//    /**
//     * Test of findAll method, of class WorktimeService.
//     */
//    @Test
//    public void testFindAll_PageInfo() {
//        System.out.println("findAll");
//        PageInfo info = null;
//        WorktimeService instance = new WorktimeService();
//        List<Worktime> expResult = null;
//        List<Worktime> result = instance.findAll(info);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findByPrimaryKey method, of class WorktimeService.
//     */
//    @Test
//    public void testFindByPrimaryKey() {
//        System.out.println("findByPrimaryKey");
//        Object obj_id = null;
//        WorktimeService instance = new WorktimeService();
//        Worktime expResult = null;
//        Worktime result = instance.findByPrimaryKey(obj_id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findByPrimaryKeys method, of class WorktimeService.
//     */
//    @Test
//    public void testFindByPrimaryKeys() {
//        System.out.println("findByPrimaryKeys");
//        Integer[] ids = null;
//        WorktimeService instance = new WorktimeService();
//        List<Worktime> expResult = null;
//        List<Worktime> result = instance.findByPrimaryKeys(ids);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findByModel method, of class WorktimeService.
//     */
//    @Test
//    public void testFindByModel() {
//        System.out.println("findByModel");
//        String modelName = "";
//        WorktimeService instance = new WorktimeService();
//        Worktime expResult = null;
//        Worktime result = instance.findByModel(modelName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findCobots method, of class WorktimeService.
//     */
//    @Test
//    public void testFindCobots() {
//        System.out.println("findCobots");
//        int obj_id = 0;
//        WorktimeService instance = new WorktimeService();
//        Set<Cobot> expResult = null;
//        Set<Cobot> result = instance.findCobots(obj_id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findCobotsAsList method, of class WorktimeService.
//     */
//    @Test
//    public void testFindCobotsAsList() {
//        System.out.println("findCobotsAsList");
//        int obj_id = 0;
//        WorktimeService instance = new WorktimeService();
//        List<Cobot> expResult = null;
//        List<Cobot> result = instance.findCobotsAsList(obj_id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findWithFullRelation method, of class WorktimeService.
//     */
//    @Test
//    public void testFindWithFullRelation() {
//        System.out.println("findWithFullRelation");
//        PageInfo info = null;
//        WorktimeService instance = new WorktimeService();
//        List<Worktime> expResult = null;
//        List<Worktime> result = instance.findWithFullRelation(info);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class WorktimeService.
//     */
//    @Test
//    public void testInsert_List() throws Exception {
//        System.out.println("insert");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.insert(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class WorktimeService.
//     */
//    @Test
//    public void testInsert_Worktime() throws Exception {
//        System.out.println("insert");
//        Worktime worktime = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.insert(worktime);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insertWithFormulaSetting method, of class WorktimeService.
//     */
//    @Test
//    public void testInsertWithFormulaSetting_List() throws Exception {
//        System.out.println("insertWithFormulaSetting");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.insertWithFormulaSetting(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insertWithFormulaSetting method, of class WorktimeService.
//     */
//    @Test
//    public void testInsertWithFormulaSetting_Worktime() throws Exception {
//        System.out.println("insertWithFormulaSetting");
//        Worktime worktime = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.insertWithFormulaSetting(worktime);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of insertSeries method, of class WorktimeService.
     */
    @Test
    @Transactional
    @Rollback(true)
    public void testInsertSeries() throws Exception {
        System.out.println("insertSeries");
        String baseModelName = "UNO-1372G-E3AE";
        List<String> seriesModelNames = newArrayList("TEST_1", "TEST2");
        int expResult = 1;
        int result = instance.insertSeries(baseModelName, seriesModelNames);
        assertEquals(expResult, result);
        
        Worktime seriesModelPojo = instance.findByModel(seriesModelNames.get(0));
        assertNotNull(seriesModelPojo);
        assertEquals(3, seriesModelPojo.getCobots().size());

    }
//
//    /**
//     * Test of update method, of class WorktimeService.
//     */
//    @Test
//    public void testUpdate_List() throws Exception {
//        System.out.println("update");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.update(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class WorktimeService.
//     */
//    @Test
//    public void testUpdate_Worktime() throws Exception {
//        System.out.println("update");
//        Worktime worktime = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.update(worktime);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of merge method, of class WorktimeService.
//     */
//    @Test
//    public void testMerge_List() throws Exception {
//        System.out.println("merge");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.merge(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of merge method, of class WorktimeService.
//     */
//    @Test
//    public void testMerge_Worktime() throws Exception {
//        System.out.println("merge");
//        Worktime worktime = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.merge(worktime);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insertByExcel method, of class WorktimeService.
//     */
//    @Test
//    public void testInsertByExcel() throws Exception {
//        System.out.println("insertByExcel");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.insertByExcel(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of mergeByExcel method, of class WorktimeService.
//     */
//    @Test
//    public void testMergeByExcel() throws Exception {
//        System.out.println("mergeByExcel");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.mergeByExcel(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of initUnfilledFormulaColumn method, of class WorktimeService.
//     */
//    @Test
//    public void testInitUnfilledFormulaColumn() {
//        System.out.println("initUnfilledFormulaColumn");
//        Worktime w = null;
//        WorktimeService instance = new WorktimeService();
//        instance.initUnfilledFormulaColumn(w);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of saveOrUpdate method, of class WorktimeService.
//     */
//    @Test
//    public void testSaveOrUpdate() throws Exception {
//        System.out.println("saveOrUpdate");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.saveOrUpdate(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class WorktimeService.
//     */
//    @Test
//    public void testDelete_List() throws Exception {
//        System.out.println("delete");
//        List<Worktime> l = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.delete(l);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class WorktimeService.
//     */
//    @Test
//    public void testDelete_Worktime() throws Exception {
//        System.out.println("delete");
//        Worktime w = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.delete(w);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class WorktimeService.
//     */
//    @Test
//    public void testDelete_IntegerArr() throws Exception {
//        System.out.println("delete");
//        Integer[] ids = null;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.delete(ids);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class WorktimeService.
//     */
//    @Test
//    public void testDelete_int() throws Exception {
//        System.out.println("delete");
//        int id = 0;
//        WorktimeService instance = new WorktimeService();
//        int expResult = 0;
//        int result = instance.delete(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of reUpdateAllFormulaColumn method, of class WorktimeService.
//     */
//    @Test
//    public void testReUpdateAllFormulaColumn() throws Exception {
//        System.out.println("reUpdateAllFormulaColumn");
//        WorktimeService instance = new WorktimeService();
//        instance.reUpdateAllFormulaColumn();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
