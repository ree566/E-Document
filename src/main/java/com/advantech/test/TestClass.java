/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.json.JSONArray;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String[] args) {
        JSONArray arr = new JSONArray();
        addSomething(arr);
        System.out.println(arr);
    }

    public static void addSomething(JSONArray arr){
        arr.put("ABBA");
    }

}
