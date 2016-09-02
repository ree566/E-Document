/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.BasicLineTypeFacade;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static int count = 0;

    public static void main(String args[]) {
        Pattern p = Pattern.compile("^[a-zA-Z]+([0-9]+).*");
        Matcher m = p.matcher("TTGGGGT233221");

        if (m.find()) {
            System.out.println(Integer.parseInt(m.group(1)));
        }
    }

    private static void showParam(BasicLineTypeFacade tF, BasicLineTypeFacade bF) {
        System.out.println("-----------------------------");
        System.out.println("tF:" + tF.getParam());
        System.out.println("bF:" + bF.getParam());
    }

    public boolean testFunction() {
        System.out.println("Function testing.");
        return true;
    }

}
