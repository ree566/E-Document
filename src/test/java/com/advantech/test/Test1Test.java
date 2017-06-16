/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import junit.framework.TestCase;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test extends TestCase {
    
    public Test1Test(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of main method, of class Test1.
     */
    public void testMain() {
        String flowRegex = "[^a-zA-Z0-9\\_\\-\\\\(\\\\)]+";
        String testString = "BAB_ASSY-T1 ";
        
        System.out.println(testString.replaceAll(flowRegex, ""));
    }
    
}
