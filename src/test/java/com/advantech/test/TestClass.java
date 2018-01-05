/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        BigDecimal b = new BigDecimal("12.5");
        BigDecimal b2 = new BigDecimal("12.0");
        BigDecimal b3 = new BigDecimal("12");
        System.out.println(isIntegerValue(b));
        System.out.println(isIntegerValue(b2));
        System.out.println(isIntegerValue(b3));
    }

    private boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }

}
