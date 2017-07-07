/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Type;
import com.advantech.model.Worktime;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        Field field = Worktime.class.getDeclaredField("type");
        field.setAccessible(true);
        
        assertTrue(field.getType().equals(Type.class));

    }

}
