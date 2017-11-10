/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.AlarmTestAction;
import com.advantech.model.TestRecord;
import com.advantech.model.TestTable;
import com.advantech.service.LineBalancingService;
import com.advantech.service.TestRecordService;
import com.advantech.service.TestService;
import com.advantech.service.TestTableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
    TestTableService testTableService;

    @Autowired
    TestService testService;

    @Autowired
    TestRecordService testRecordService;
    
    @Autowired
    LineBalancingService lineBalancingService;
    
    @Test
    @Transactional
    @Rollback(false)
    public void testAlarm(){
        AlarmTestAction at = new AlarmTestAction("T38", 1);
        sessionFactory.getCurrentSession().update(at);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFindTestRecord() throws JsonProcessingException {
        assertNotEquals(0, testRecordService.findAll().size());
    }
    
//    @Test
    @Transactional
    @Rollback(true)
    public void testFindTestTable() throws JsonProcessingException{
        assertNotNull(testTableService.findByPrimaryKey(1));
    }
    
//    @Test
    @Transactional
    @Rollback(true)
    public void testFindTest() throws JsonProcessingException{
        assertNotNull(testService.findByPrimaryKey(1));
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test1() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        HibernateObjectPrinter.print(session.get(TestTable.class, 1));
        HibernateObjectPrinter.print(session.get(com.advantech.model.Test.class, 1));
        HibernateObjectPrinter.print(session.get(TestRecord.class, 1));
    }
    
//    @Test
    @Transactional
    @Rollback(true)
    public void test3() throws JsonProcessingException {
        HibernateObjectPrinter.print(lineBalancingService.findByPrimaryKey(3997));
        
    }

}
