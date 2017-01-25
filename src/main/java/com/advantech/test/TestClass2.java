/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;
import java.util.Scanner;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    public static void main(String arg0[]) {
        Scanner input = new Scanner(System.in);
        out.print("請輸入數字:");
        String str1 = input.nextLine();
        int count = 0;
        try {
            do {
                count++;
                Integer a = Integer.parseInt(str1);
                Integer b = Integer.parseInt(new StringBuffer(str1).reverse().toString());
                Integer c = a + b;
                out.println(a + "+" + b + "=" + c);

                if (new StringBuffer(c.toString()).reverse().toString().equals(c.toString())) {
                    out.println("迴文出現了 ! 共計算" + count + "次");
                    break;
                } else {
                    str1 = c.toString();
                }
            } while (count <= 100);
        } catch (Exception e) {
            out.println("The value is out of range!");
        }
    }

}
