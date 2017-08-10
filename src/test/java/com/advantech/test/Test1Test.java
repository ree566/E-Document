/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.math.BigDecimal;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

//    @Test
    public void test() throws Exception {
        BigDecimal five = new BigDecimal("5");
        BigDecimal ten = new BigDecimal("10");
        BigDecimal zero = BigDecimal.ZERO;

        assertEquals(new BigDecimal("2"), ten.divide(five));
//        assertEquals(new BigDecimal("5"), ten.divide(five).divide(zero));
        assertEquals(new BigDecimal("6"), ten.add(five.divide(five)));
        
    }

}
