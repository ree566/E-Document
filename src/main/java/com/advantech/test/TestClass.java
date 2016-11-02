/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.ActionCodeMapping;
import static java.lang.System.out;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) throws Exception {
        int specCharIndex = 34;
        String str1 = "2014 FIFA World Cup Final Winning germany Team";
        out.println("Before special character toUpperCase is: " + str1);

        char ch = str1.charAt(specCharIndex); //g是要更換為大寫的字母
        char upperCh = Character.toUpperCase(ch);

        out.println("This is the word you want to upperCase: " + ch + " -> " + upperCh); 

        for (int i = 0, j = str1.length(); i <= j; i++) {  
            if (i == specCharIndex) {
                String nString = str1.substring(0, specCharIndex) + upperCh + str1.substring(specCharIndex + 1, j);
                out.println("The string after upperCase is: " + nString);
                break;
            }
        }
    }

    private String getResponseUser(ActionCodeMapping am) {
        return null;
    }
}
