/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

/**
 *
 * @author Wei.Cheng
 */
public class ParamChecker {

    public ParamChecker() {
    }

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
    
    public static void main(String[] args){
        ParamChecker p = new ParamChecker();
        System.out.println(p.checkInputVals("   ","B","C"));
    }
}
