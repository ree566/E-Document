/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String[] args) {
        List<Contract> objs = new ArrayList();
        Contract c1 = new Super();
        Contract c2 = new Sub(); //Line n1
        Super s1 = new Sub();
        objs.add(c1);
        objs.add(c2);
        objs.add(s1); //Line n2

        for (Object itm : objs) {
            System.out.println(itm.getClass().getName());
        }
    }

}

interface Contract {
}

class Super implements Contract {
}

class Sub extends Super {
}
