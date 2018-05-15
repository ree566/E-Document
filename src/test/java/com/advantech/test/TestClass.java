/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testStopWatch() throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            StopWatch clock = new StopWatch("clock_" + (i + 1));
            clock.start();
            temp_L.add(clock);
        }

        Thread.sleep(5000);

        temp_L.forEach(clock -> {
            clock.stop();
            System.out.println(clock.prettyPrint());
        });

    }

}
