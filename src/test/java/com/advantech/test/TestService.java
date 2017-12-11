/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Bab;
import com.advantech.service.BabService;
import com.advantech.service.LineBalancingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {
    
    @Autowired
    private BabService babService;
    
    @Autowired
    private LineBalancingService lineBalancingService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void testLineBalancingService(){
        Bab b = babService.findByPrimaryKey(10870);
        lineBalancingService.insert(b);
    }
    
}
