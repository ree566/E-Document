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
import com.advantech.model.BabStatus;
import com.advantech.model.CountermeasureEvent;
import com.advantech.model.Line;
import com.advantech.model.ReplyStatus;
import com.advantech.model.User;
import com.advantech.service.BabPcsDetailHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class HibernateTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private AlarmTestActionDAO alarmTestActionDAO;

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private com.advantech.dao.TestDAO testDAO;

    @Autowired
    private BabPcsDetailHistoryService pcsHistoryService;

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
    public void testSqlFunction() throws JsonProcessingException {
        Object o = sqlViewDAO.findBabAvg(10870);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFbn() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Query q = session
                .createQuery("from TagNameComparison t where t.id.lampSysTagName = 'LA-S-1'")
                .setMaxResults(1);
        HibernateObjectPrinter.print(q.list());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testClone() throws Exception {
        List<Bab> l = babDAO.findProcessingByLine(7);
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testHql() throws JsonProcessingException {
        String hql = "select new Map(b.id as id, b.po as po, b.modelName as modelName, "
                + "l.name as lineName, f.name as floorName) "
                + "from Bab b left join b.line l left join l.floor f";
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        q.setMaxResults(10);
        List l = q.list();
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void chartTest() throws JsonProcessingException {
        List l = pcsHistoryService.findByBabForMap(12595);
        HibernateObjectPrinter.print(this.toChartForm(l));
    }

    private Map toChartForm(List<Map> l) {
        List<Map<String, Object>> total = new ArrayList();
        int diffSum = 0;
        int maxGroup = 0;
        for (Map m : l) {
            String tagName = (String) m.get("tagName");
            Integer groupid = (Integer) m.get("groupid");
            Integer diff = (Integer) m.get("diff");
            Map filter = total.stream()
                    .filter(i -> i.containsKey("name") && i.get("name").equals(tagName))
                    .findFirst().orElse(null);
            if (filter == null) {
                Map tagInfo = new HashMap();
                tagInfo.put("name", tagName);
                List dataPoints = new ArrayList();
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", diff);
                tagInfo.put("dataPoints", dataPoints);
                total.add(tagInfo);
            } else {
                List dataPoints = (List) filter.get("dataPoints");
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", m.get("diff"));
                dataPoints.add(dataPoint);
            }
            diffSum += diff;
            if (maxGroup < groupid) {
                maxGroup = groupid;
            }
        }

        int people = total.size();

        Map infoWithAvg = new HashMap();
        infoWithAvg.put("avg", (diffSum / people / maxGroup));
        infoWithAvg.put("data", total);
        return infoWithAvg;
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testConverter() {
        Bab bab = (Bab) sessionFactory.getCurrentSession().get(Bab.class, 12730);
        assertNotNull(bab);
        assertNotNull(bab.getBabStatus());
        assertEquals(bab.getBabStatus(), BabStatus.NO_RECORD);
        assertNotNull(bab.getReplyStatus());
        assertEquals(bab.getReplyStatus(), ReplyStatus.NO_NEED_TO_REPLY);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testTagNameCompar() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Line line = session.get(Line.class, 1);
        session.save(new Bab("test", "test", line, 2, 1, 1));
    }
}
