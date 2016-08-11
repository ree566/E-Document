/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class StringParser {

    private static final Logger log = LoggerFactory.getLogger(StringParser.class);

    public static int strToInt(String str) {
        return ((str == null || "".equals(str)) ? 0 : Integer.parseInt(str));
    }

    public static Double strToDouble(String str) {
        return ((str == null || "".equals(str)) ? 0.0 : Double.parseDouble(str));
    }
}
