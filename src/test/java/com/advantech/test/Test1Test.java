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
import java.util.Objects;
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

//    @Test
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

//    @Test
    public void testStringSplit() {
        String sidx = "col1 asc, col2 desc, id";
        String sidx2 = "id";
        String sidx3 = "id asc";

        String[] result = sidx.split(",");
        String[] result2 = sidx2.split(",");
        String[] result3 = sidx3.split(",");

        System.out.println(trimLeftRight(result[0]));
        System.out.println(trimLeftRight(result[1]));
        System.out.println(trimLeftRight(result[2]));
    }

    private String trimLeftRight(String s) {
        return s.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

//    @Test
    public void testString() {
        String test1 = "IMC-719-2AC";
        String test2 = "IMC-719-2AC";
        System.out.println(Objects.equals(test1, test2));
    }

    @Test
    public void testString2() {
        String test1 = "1;2;3;";
        String test2 = "1;2;3";

        String[] i = test1.split(";");
        int len = (int) Arrays.stream(i).filter(o -> !"".equals(o)).count();
        System.out.println(len);
        
        String[] i2 = test2.split(";");
        int len2 = (int) Arrays.stream(i2).filter(o -> !"".equals(o)).count();
        System.out.println(len2);
    }
    
    @Test
    public void testDecimal() {
        BigDecimal b = BigDecimal.ZERO;
    }
}
