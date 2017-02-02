/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.PropertiesReader;
import static java.lang.System.out;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    public static void main(String arg0[]) {
        PropertiesReader p = PropertiesReader.getInstance();
        out.println(p.toString());
        
    }
    
    private static String[] separateMailLoop(String mailString){
        return mailString == null ? new String[0] : mailString.replace(" ", "").split(",");
    }

}
