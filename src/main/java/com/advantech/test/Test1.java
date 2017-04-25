/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.joda.time.DateTime;

/**
 *
 * @author Wei.Cheng
 */
public class Test1 {

    public static void main(String arg0[]) {
        String date = "17/06/01";
        DateTime d = new DateTime(date);
        System.out.println(d.toString());
    }

}
