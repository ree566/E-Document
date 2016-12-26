/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.ArrayList;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    public static void main(String arg0[]) {
        System.out.println("\n字串===========");
        String array4 = "MSPO";
        new TestClass<String>(array4);
    }
}

class Cla {

    String s;

    Cla(String s) {
        this.s = s;
    }

    @Override
    public String toString() {//改寫toString，不然會引出記憶體位址
        return s;
    }
}
