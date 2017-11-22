/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.webservice.root.Section;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() {
        for (Section sec : Section.values()) {
            System.out.print(sec.toString());
            System.out.print(" ");
            System.out.println(sec.getCode());
        }
    }

}
