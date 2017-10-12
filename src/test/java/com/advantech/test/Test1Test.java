/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.WorktimeService;
import static junit.framework.Assert.*;
import org.hibernate.SessionFactory;
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
public class Test1Test {

    @Value("${WORKTIME.UPLOAD.SOP}")
    private boolean sopUpload;

    @Value("${HIBERNATE.JDBC.BATCHSIZE}")
    private int batchSize;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private WorktimeService worktimeService;

    @Test
    public void test() {
        assertTrue(sopUpload);
        assertEquals(20, batchSize);
        assertNotNull(worktimeService);
    }

}
