/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

//    @Test
    public void test() throws Exception {
        int count = 1;
        int retryTimes = 3;

        while (true) {
            try {
                testFunc();
                break;
            } catch (Exception e) {
                System.out.println("Begin retry. " + count);
                if (count++ == retryTimes) {
                    throw e;
                }
            }
        }

    }

    private void testFunc() throws Exception {
        throw new Exception("Exception detect");
    }

}
