/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

//    @Test
    public void test() {
        String[] st = {"   test1   ", "   test2", "test3   ", "   te st4   ", "test5", "te st6"};

        System.out.println(Arrays.toString(st));

        List<String> l = Arrays.stream(st).map(s -> {
            return removeModelNameExtraSpaceCharacter(s);
        }).collect(Collectors.toList());

        System.out.println(l);

        l.forEach(t -> {
            System.out.println(t.length());
        });
    }

    private void removeModelNameExtraSpaceCharacter(Worktime w) {
        String modelName = w.getModelName();
        w.setModelName(removeModelNameExtraSpaceCharacter(modelName));
    }

    private String removeModelNameExtraSpaceCharacter(String modelName) {
        return modelName.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

    @Test
    public void test2() {
        Class c = Worktime.class;
        Field fields[] = c.getDeclaredFields();
        for (Field field : fields) {
            if (!Collection.class.isAssignableFrom(field.getType())) {
                System.out.println(" " + field.getName());
            }
        }
    }

}
