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
    
    static int i = 1, j = i;

    public static void main(String arg0[]) {
        j = 5;
        TestClass2 t = new TestClass2();
        System.out.println(i);
        System.out.println(j);
    }

    public static void Foo(StringBuilder fooSB){
        fooSB.append("test");
        fooSB = null;
    }
}


