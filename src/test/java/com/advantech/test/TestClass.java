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
        System.out.println(Double.compare(0.85, 0.8));
    }

    private boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }

}
