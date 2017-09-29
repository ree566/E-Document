/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.SopQueryPort;
import javax.transaction.Transactional;
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
//    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryPortTest {
    
    @Autowired
    private SopQueryPort sopQueryPort;

    @Autowired
    private WorktimeService worktimeService;

    @Transactional
    @Rollback(false)
    @Test
    public void test() throws Exception {
        Worktime w = worktimeService.findByPrimaryKey(17915);
        w.setAssyPackingSop("test1");
        w.setTestSop("test2");
        worktimeService.merge(w);
    }
}
