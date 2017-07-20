/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() throws SecurityException, NoSuchFieldException, JAXBException {
        List l = newArrayList("A","b","C");
        System.out.println(l.toString());
    }

}
