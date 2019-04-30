/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

//    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal(1.53);
        BigDecimal b2 = new BigDecimal(1.53);
        System.out.println(b1.compareTo(b2) == 0);
    }

    @Test
    public void testLocalJson() throws IOException {
        String filePath = "C:\\Users\\wei.cheng\\Documents\\NetBeansProjects\\E-Document\\src\\main\\webapp\\json\\flow.json";
        ObjectMapper mapper = new ObjectMapper();
        List<Map> l = Arrays.asList(mapper.readValue(new File(filePath), Map[].class));
        int index = 0;
        for (Map m : l) {
            m.put("id", index++);
        }

        String json = new Gson().toJson(l);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(json);
            System.out.println("Successfully Copied JSON Object to File...");
        }
    }

}
