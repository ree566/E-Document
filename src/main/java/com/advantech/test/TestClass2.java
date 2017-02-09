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

    protected class Nested {

        public void method() {
        }
    }

    public static void main(String arg0[]) {

    }

}

class SubTopLevel extends TestClass2 {

    static void Foo() {

        new TestClass2().new Nested().method();
    }
}
