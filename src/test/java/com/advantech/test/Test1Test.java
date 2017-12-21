/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() {
        Character n = 'N';
        String sN = "N";
        System.out.println(ObjectUtils.compare(n.toString(), sN));
    }

}
