/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

//    @Test
    public void test(){
        
        Map<String, String> m = new HashMap();
        m.put("test", "testString");
        
        System.out.println(m.toString());
    }
}
