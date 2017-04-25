/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import static com.advantech.helper.MD5Encoder.toMD5;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Wei.Cheng
 */
public class PasswordEncoder {

    private static final String SALT_MESSAGE = "Hello world!";

    public static String encryptPassord(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return isPasswordVaild(password) ? toMD5(SALT_MESSAGE + password) : null;
    }

    public static boolean isPasswordHashMatches(String origin, String encrypt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return isPasswordVaild(origin) ? toMD5(SALT_MESSAGE + origin).equals(encrypt) : false;
    }

    private static boolean isPasswordVaild(String password) {
        return password != null && !"".equals(password.trim());
    }
}
