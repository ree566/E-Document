/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.google.gson.Gson;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private String attr1;
    private Integer attr2;
    private String attr3;

    public TestClass() {
    }

    public TestClass(String attr1, Integer attr2, String attr3) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public Integer getAttr2() {
        return attr2;
    }

    public void setAttr2(Integer attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static void main(String[] args) {
        TestClass c = new TestClass("1", 3, "2");
        System.out.println(c.toString());
    }

}


