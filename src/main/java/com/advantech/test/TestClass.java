/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) throws Exception {
        int len = 25;
        int a = 0;
        int b;
        while (a <= len) {
            a++;
            b = len;
            if (a % 2 == 0) {
                while (b > 0) {
                    out.print(" ");
                    out.print("*");
                    b--;
                }
            } else {
                while (b > 0) {
                    out.print("*");
                    out.print(" ");
                    b--;
                }
            }
            out.println();
        }
    }

}
