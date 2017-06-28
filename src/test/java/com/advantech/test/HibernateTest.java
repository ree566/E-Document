/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.User;
import com.advantech.service.UserNotificationService;
import com.advantech.service.UserService;
import java.util.List;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
    "classpath:servlet-context.xml",
    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateTest {
    
    @Autowired
    private UserNotificationService userNotificationService;
    
    @Autowired
    private UserService userService;

    //CRUD testing.
    @Transactional
    @Rollback(true)
    @Test
    public void testFindUser() throws Exception {
        List<User> l = userNotificationService.findUsersByNotification("abc");
        
        assertTrue(l.isEmpty());
        
        l = userNotificationService.findUsersByNotification("worktime_alarm");
        
        assertEquals(l.size(), 2);
        
        User gavin = userService.findByPrimaryKey(47);
        assertTrue(l.contains(gavin));
    }
    
}
