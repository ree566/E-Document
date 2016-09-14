/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wei.Cheng
 */
public class CountermeasureDAOTest {
    
    public CountermeasureDAOTest() {
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
     * Test of getCountermeasureView method, of class CountermeasureDAO.
     */
    @Test
    public void testGetCountermeasureView() {
        System.out.println("getCountermeasureView");
        CountermeasureDAO instance = new CountermeasureDAO();
        List<Map> expResult = null;
        List<Map> expResult2 = new ArrayList();
        List<Map> result = instance.getCountermeasureView();
        assertNotEquals(expResult, result);
        assertNotEquals(expResult, expResult2);
       
    }
}
