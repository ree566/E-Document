/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.service.SystemReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotEquals;
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
    SystemReportService cService;
    
    @Test
    @Transactional
    @Rollback(false)
    public void testAlarm() throws JsonProcessingException{
        List l = cService.getPersonalAlmForExcel("2017-08-01", "2017-08-02");
        HibernateObjectPrinter.print(l);
        assertTrue(l.get(0) instanceof Map);
        assertNotEquals(0, l.size());
    }



}
