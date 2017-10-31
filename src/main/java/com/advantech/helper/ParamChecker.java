/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class ParamChecker {

    public boolean checkInputVal(String args) {
        return args != null && !"".equals(args.trim());
    }

    public boolean checkInputVals(String... args) {
        boolean b = true;
        for (String s : args) {
            b = b & checkInputVal(s);
        }
        return b;
    }
    
    public boolean checkArray(Object[] arr) {
       return arr != null && arr.length != 0;
    }
    
    public static void main(String[] args){
        ParamChecker p = new ParamChecker();
        System.out.println(p.checkInputVals("   ","B","C"));
    }
}
