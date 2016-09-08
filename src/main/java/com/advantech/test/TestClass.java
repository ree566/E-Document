/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.BasicLineTypeFacade;
import javax.management.RuntimeErrorException;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static int count = 0;

    public static void main(String args[]) throws Exception {
//        BasicLineTypeFacade tF = TestLineTypeFacade.getInstance();
        throwRuntime();
    }
    
    private static void throwRuntime() throws Exception{
        throw new Exception("Nyee BuJam Dao Wo Do Exception Ler.");
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
