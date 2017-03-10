/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.MD5Encoder;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Wei.Cheng
 */
public class Test1 {

    public static void main(String arg0[]) {
        try {
            String str = MD5Encoder.toMD5("sysop");
            System.out.println(str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            System.out.println(ex);
        }
    }
}
