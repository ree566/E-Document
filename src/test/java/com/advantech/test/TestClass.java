/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.BabDataCollectMode;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static final Logger log = LoggerFactory.getLogger(TestClass.class);

    List<StopWatch> temp_L = new ArrayList();

//    @Test
    public void test() {
        log.info("bab_id {} / Max: {} / Sum: {} / BANANCE: {} / STANDARD: {}", 1, 0.1,
                0.2, 0.3, 0.4);
    }

//    @Test
    public void testKeywordFilter() throws InterruptedException {
        List<String> keywords = newArrayList("TPC", "T1PC1", "ABCC", "T1PC1331", "DBB");
        String modelName = "TPC1331-2213-ZZ";

        String key = keywords.stream()
                .filter(modelName::contains)
                .max(Comparator.comparing(String::length)).orElse(null);
        System.out.println(key);
    }

//    @Test
    public void testEnum() {
        int a = 3;
        long b = a;
        System.out.println(b);
    }

//    @Test
    public void testString() {
        String str = "TPAB810807";
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(str);
        String st = null;
        while (m.find()) {
            st = m.group();
        }
        str = str.replace(st, Integer.toString((NumberUtils.createInteger(st)) - 1));
        System.out.println(str);
    }

    @Test
    public void testEnum2() {
        System.out.println(BabDataCollectMode.valueOf("MAsNUAL"));
    }

}
