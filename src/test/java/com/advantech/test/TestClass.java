/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.converter.Encodeable;
import com.advantech.model.BabStatus;
import com.advantech.security.State;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        Object a = "1";
        System.out.println(Encodeable.forToken(BabStatus.class, 1));
    }


}
