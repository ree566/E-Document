/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    public static void main(String arg0[]) {
        Integer i = 240;
        Double d = 80 * 4.0;
        System.out.println(i < d);

    }

    private static String[] separateMailLoop(String mailString) {
        return mailString == null ? new String[0] : mailString.replace(" ", "").split(",");
    }

}
