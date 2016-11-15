/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.BAB;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) {
        BAB b1 = new BAB();
        BAB b2 = new BAB();
        b1.setId(1999);
        b1.setModel_name("1");
        b2.setId(19991);
        b1.setModel_name("2");
        
        
        Map m = new HashMap();
        m.put("test", b1);
        
        out.println(m.containsValue(b2));
    }
}
