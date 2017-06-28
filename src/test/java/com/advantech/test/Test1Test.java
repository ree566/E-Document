/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.controller.CrudAction;
import junit.framework.TestCase;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test extends TestCase {

    /**
     * Test of main method, of class Test1.
     */
    public void testMain() {
        CrudAction action = CrudAction.valueOf("ADD");
        
        assertEquals(action, CrudAction.ADD);
    }
    
}
