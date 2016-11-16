/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.awt.Desktop;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    private static final Logger log = LoggerFactory.getLogger(TestClass2.class);

    public static void main(String arg0[]) {

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://172.20.131.225/mesdashboard/"));
            } catch (Exception ex) {
            }
        }
    }
}
