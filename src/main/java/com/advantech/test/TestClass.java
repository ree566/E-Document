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
        Double d = null;
        JSONObject obj = new JSONObject();
        obj.put("data", d);
        
        Double dd = obj.getDouble("data");
        
        out.println(dd);
    }
}
