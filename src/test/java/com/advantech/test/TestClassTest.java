/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.ArrayList;
import java.util.List;
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
public class TestClassTest {

    public TestClassTest() {
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
     * Test of main method, of class TestClass.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        TestClass.main(args);
    }

    @Test
    public void testFunction() {
        System.out.println("testFunction");
        List result = new TestClass().testList();
        List except = new ArrayList();
        assertEquals(result, except);
    }

}
