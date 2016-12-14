/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String arg0[]) {
        JSONObject obj = new JSONObject();
        JSONObject obj2 = new JSONObject();
        
        obj.put("abb", 1);
        obj2.put("abb", 1);
        
        out.println(obj.equals(obj2));
    }
}
