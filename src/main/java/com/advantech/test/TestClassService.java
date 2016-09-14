/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class TestClassService {
    
    private final TestClass t;

    public TestClassService() {
        t = new TestClass();
    }

    
    public static void main(String arg[]) {

        JSONObject obj1 = new JSONObject();
        obj1.put("data", 1);
        JSONObject obj2 = new JSONObject();
        obj2.put("data", 1);
        System.out.print(obj1.equals(obj2));
    }
}
