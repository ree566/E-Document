/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static junit.framework.Assert.assertEquals;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() throws Exception {
        String tar = "2017-08-02";
        DateTime d = new DateTime().minusDays(1);
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        
        String result = df.print(d);

        assertEquals(tar, result);
    }

    private void testFunc() throws Exception {
        throw new Exception("Exception detect");
    }

}
