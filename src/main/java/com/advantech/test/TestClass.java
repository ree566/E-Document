/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.BasicLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static int count = 0;

    public static void main(String args[]) {
//        BasicLineTypeFacade tF = TestLineTypeFacade.getInstance();
        boolean b = true;

        for (int i = 0; i <= 10; i++) {
            BasicLineTypeFacade bF = BabLineTypeFacade.getInstance();
            System.out.println("The " + i + " time data : " + bF.getParam());

            if (i % 2 == 0) {
                b = !b;
                bF.setParam(b);
            }
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
