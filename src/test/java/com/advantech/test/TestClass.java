/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.BAB;
import com.advantech.service.BABService;
import com.google.gson.Gson;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
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
public class TestClass {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private BABService babService;
    
    @Before
    public void init(){
        assertNotNull(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        BAB b = babService.getBAB(8493);
        System.out.println(new Gson().toJson(b));
    }
}
