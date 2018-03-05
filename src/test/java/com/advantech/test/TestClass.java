/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {
    
    private static final Logger log = LoggerFactory.getLogger(TestClass.class);

    @Test
    public void test() {
        log.info("bab_id {} / Max: {} / Sum: {} / BANANCE: {} / STANDARD: {}", 1, 0.1,
                        0.2, 0.3, 0.4);
    }

}
