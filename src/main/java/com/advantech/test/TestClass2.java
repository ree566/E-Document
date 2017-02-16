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
public class TestClass2 {

    public static void main(String arg0[]) {
        JSONObject obj = new JSONObject();
        JSONObject innerObj = obj.getJSONObject("someKey");
        out.println(innerObj == null ? "Empty" : innerObj);
    }

}

