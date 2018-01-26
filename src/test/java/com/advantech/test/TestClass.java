/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        Map m = new HashMap();
        m.put("a", 20);
        m.put("c", "GG");
        m.put("b", "B");
        m.put("d", new BigDecimal(20));
        m.put("e", 1);
        
        m.replace("b", "bCCA");
        
        System.out.println(m.toString());
    }


}
