/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.AlarmTestActionDAO;
import com.advantech.dao.BabDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.CountermeasureEvent;
import com.advantech.model.User;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import static org.junit.Assert.*;
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
    private AlarmTestActionDAO alarmTestActionDAO;

    @Autowired
    private BabDAO babDAO;

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

//    @Test
    @Transactional
    @Rollback(true)
    public void test2() throws JsonProcessingException {
        Object o = alarmTestActionDAO.findByPrimaryKey("T1");
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test3() throws JsonProcessingException {
        Object o = babDAO.findByPrimaryKey(11035);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlQuery() {
        Query q = sessionFactory.getCurrentSession().createSQLQuery("select t.* from testView t");
        q.setResultTransformer(Transformers.aliasToBean(Bab.class));
        List l = q.list();
        assertEquals(10, l.size());
        assertTrue(l.get(0) instanceof Bab);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQuery() throws JsonProcessingException {
        testObject(
//                Floor.class,
                User.class,
//                UserProfile.class,
//                UserNotification.class,
//                Unit.class,
//                TestTable.class,
//                com.advantech.model.Test.class,
//                TestRecord.class,
//                AlarmTestAction.class,
//                AlarmBabAction.class,
//                Line.class,
//                LineType.class,
//                LineTypeConfig.class,
//                Bab.class,
//                Fbn.class,
//                BabPcsDetailHistory.class,
//                BabSettingHistory.class,
//                BabBalanceHistory.class,
//                BabAlarmHistory.class,
//                Countermeasure.class,
//                CountermeasureEvent.class,
//                ActionCode.class,
//                ErrorCode.class,
                CountermeasureEvent.class
        );

    }

    private void testObject(Class... cls) {
        Session session = sessionFactory.getCurrentSession();
        for (Class c : cls) {
            System.out.println(c);
            Object o = session.createCriteria(c).setMaxResults(1).uniqueResult();
            assertNotNull(o);
//            assertEquals(o.getClass(), c);
            System.out.println(c + " Test pass");
        }
    }
    
    @Autowired
    private SqlViewDAO sqlViewDAO;
    
//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlFunction() throws JsonProcessingException{
        Object o = sqlViewDAO.findBabAvg(10870);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFbn() throws JsonProcessingException{
        Session session = sessionFactory.getCurrentSession();
        Query q = session
                .createQuery("from TagNameComparison t where t.id.lampSysTagName = 'LA-S-1'")
                .setMaxResults(1);
        HibernateObjectPrinter.print(q.list());
    }
}
