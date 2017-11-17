/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    private enum Section {
        A("PREASSY"),
        B("BAB"),
        T("TEST"),
        P("PACKAGE");

        private final String state;

        private Section(final String state) {
            this.state = state;
        }
        
        public String getState(){
            return this.state;
        }

    }

    @Test
    public void test() {
        Section bab = Section.B;
        System.out.println(bab.getState());
        System.out.println(bab.toString());
    }

}
