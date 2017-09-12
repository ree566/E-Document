/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() {
        DateTimeFormatter dtf  = DateTimeFormat.forPattern("yy/MM/dd");
        System.out.println(dtf.print(new DateTime()));
    }
}
