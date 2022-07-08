/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.advantech.controller;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class WorktimeBatchModControllerTest {

    public WorktimeBatchModControllerTest() {
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
     * Test of initValidator method, of class WorktimeBatchModController.
     */
    @Test
    public void testInitValidator() {
        System.out.println("initValidator");
        WorktimeBatchModController instance = new WorktimeBatchModController();
        instance.initValidator();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of batchInsert method, of class WorktimeBatchModController.
     */
    @Test
    public void testBatchInsert() throws Exception {
        System.out.println("batchInsert");
        MultipartFile file = null;
        WorktimeBatchModController instance = new WorktimeBatchModController();
        String expResult = "";
        String result = instance.batchInsert(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of batchUpdate method, of class WorktimeBatchModController.
     */
    @Test
    public void testBatchUpdate() throws Exception {
        System.out.println("batchUpdate");
        MultipartFile file = null;
        WorktimeBatchModController instance = new WorktimeBatchModController();
        String expResult = "";
        String result = instance.batchUpdate(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of batchDelete method, of class WorktimeBatchModController.
     */
    @Test
    public void testBatchDelete() throws Exception {
        System.out.println("batchDelete");
        MultipartFile file = null;
        WorktimeBatchModController instance = new WorktimeBatchModController();
        String expResult = "";
        String result = instance.batchDelete(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reUpdateAllFormulaColumn method, of class
     * WorktimeBatchModController.
     */
    @Test
    public void testReUpdateAllFormulaColumn() throws Exception {
        System.out.println("reUpdateAllFormulaColumn");
        WorktimeBatchModController instance = new WorktimeBatchModController();
        boolean expResult = false;
        boolean result = instance.reUpdateAllFormulaColumn();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Autowired
    WorktimeBatchModController instance;
            
    @Test
    public void testTransToWorktimes() throws Exception {
        System.out.println("transToWorktimes");
        File initialFile = new File("C:\\Users\\wei.cheng\\Desktop\\worktime_output.xls");
        InputStream targetStream = new FileInputStream(initialFile);
        List<Worktime> result = instance.transToWorktimes(targetStream, false);
        assertEquals(1, result.size());
        HibernateObjectPrinter.print(result.get(0));
        HibernateObjectPrinter.print(result.get(0).getCobots());
    }

}
