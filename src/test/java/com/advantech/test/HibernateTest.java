/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.User;
import com.advantech.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
public class HibernateTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    UserService userService;

//    @Test
    @Transactional
    @Rollback(true)
    public void test() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("select u from User u join u.userNotifications notifi where notifi.id = 1");
        List l = q.list();
        assertTrue(l.get(0) instanceof User);
        assertEquals(13, l.size());
        HibernateObjectPrinter.print(l);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test2() throws JsonProcessingException {
        List l = userService.findLineOwnerBySitefloor(2);
        HibernateObjectPrinter.print(l);
    }

}
