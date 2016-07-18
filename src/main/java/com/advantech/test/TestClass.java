/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass{

    private static int count = 0;

    public static void main(String args[]) {
//        int cnt = 0, n = 25462;
//        int[] arr = {1, 3, 5, 7, 9};
//
//        for (; n != 0; cnt++) {
//            arr[cnt] = n % 10;
//            n = n / 10;
//        }
//        System.out.println("cnt=" + cnt);
        Test t = new Test();
        t.setTableName("T5555");
        System.out.println(t.getTableNum());
//        for (int i = 0; i < arr.length; i++) {
//            System.out.print(arr[i]);
//        }

    }

    public boolean testFunction() {
        System.out.println("Function testing.");
        return true;
    }


}
