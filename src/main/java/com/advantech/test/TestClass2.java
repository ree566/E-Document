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
public class TestClass2 {

    public static void main(String arg0[]) {
        Child child = new Child();
        Child2 child2 = new Child2();
        
        out.println(child.b);
        out.println(child2.b);
        
        child.b = 40;
        
        out.println(child.b);
        out.println(child2.b);
        
        child = new Child();
        
        out.println(child.b);
    }

}

class Father{
    static int a = 10;
    int b = 20;
}

class Child extends Father{}

class Child2 extends Father{}