/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.LineBalancing;
import com.google.gson.Gson;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        LineBalancing b = new LineBalancing();
        BeanUtils.setProperty(b, "sop4", "test");
        BeanUtils.setProperty(b, "line_id", "20");
        System.out.println(new Gson().toJson(b));
    }
}
