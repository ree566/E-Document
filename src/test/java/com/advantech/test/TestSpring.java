/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.view.BabAvg;
import com.advantech.service.LineBalancingService;
import com.advantech.service.SqlViewService;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSpring {

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private LineBalancingService lineBalancingService;

    private final double DELTA = 1e-15;
    
    @Value("${test.productivity.bypass.hours: 0}")
    private Integer[] testByPassHours;

//    @Test
    public void testLineBalanceCount() {
        List<BabAvg> l = sqlViewService.findBabAvgInHistory(13220);
//        assertEquals(3, l.size());

        double max = l.stream().mapToDouble(i -> i.getAverage()).max().getAsDouble();

//        assertEquals(830.667, max, DELTA);
        double sum = l.stream().mapToDouble(i -> i.getAverage()).sum();

        System.out.printf("sum: %.3f and max: %.3f \n", sum, max);

        double balance = lineBalancingService.caculateLineBalance(l);

        System.out.printf("balance: %.3f \n", balance);
    }

    @Test
    public void testContextParam() {
//        assertTrue(testByPassHours == null);
        System.out.println(Arrays.toString(this.testByPassHours));
    }

}
