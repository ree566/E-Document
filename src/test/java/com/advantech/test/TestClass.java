/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static com.google.common.collect.Lists.newArrayList;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        List<String> l = newArrayList("test", "bab", "gg", "312", "YY", "tTTa");
        l = l.stream().filter(str -> str.contains("t")).collect(Collectors.toList());
        assertEquals(2, l.size());
        l = l.stream().filter(str -> str.equals("tTTa")).collect(Collectors.toList());
        assertEquals(1, l.size());
        System.out.println(l);
    }

}
