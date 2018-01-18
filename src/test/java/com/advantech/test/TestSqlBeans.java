/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabAlarmHistory;
import com.advantech.model.Countermeasure;
import com.advantech.model.Floor;
import com.advantech.model.Line;
import com.advantech.model.LineType;
import com.advantech.model.TestTable;
import com.advantech.model.Unit;
import com.advantech.model.User;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.Session;
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
@Transactional
@Rollback(true)
public class TestSqlBeans {

    @Autowired
    SessionFactory sessionFactory;

    Session session;

    @Before
    public void init() {
        session = sessionFactory.getCurrentSession();
    }

//    @Test
    public void testFloor() {
        assertNotNull(session.get(Floor.class, 1));
    }

//    @Test
    public void testUser() {
        User user = (User) session.get(User.class, 1);
        assertNotNull(user);
        assertEquals(1, user.getUserProfiles().size());
        assertEquals(1, user.getUserNotifications().size());
        Unit u = user.getUnit();
        assertNotNull(u);
        assertEquals("MFG", u.getName());
    }

//    @Test
    public void testTestLine() {
        TestTable table = (TestTable) session.get(TestTable.class, 1);
        assertNotNull(table);
        assertEquals("5", table.getFloor().getName());
        assertEquals(1, table.getTests().size());
        assertEquals(1482, table.getTestLineTypeRecords().size());
    }

//    @Test
    public void testBabLine() {
        Line line = (Line) session.get(Line.class, 1);
        assertNotNull(line);
        LineType lineType = line.getLineType();
        assertNotNull(lineType);
        assertEquals("ASSY", lineType.getName());
        assertEquals(2, lineType.getLineTypeConfigs().size());
        assertEquals(955, line.getBabs().size());
        Bab bab = (Bab) session.get(Bab.class, 10790);
        assertNotNull(bab);
        assertEquals(20, bab.getFbns().size());
        assertEquals(8, bab.getBabPcsDetailHistorys().size());
        assertEquals(2, bab.getBabSettingHistorys().size());
        assertEquals(4, bab.getBabBalanceHistorys().size());
        assertEquals(0, bab.getBabAlarmHistorys().size());
        BabAlarmHistory bAlarm = (BabAlarmHistory) session.get(BabAlarmHistory.class, 6049);
        assertEquals(4, bAlarm.getFailPcs());
        Set<Countermeasure> s = bab.getCountermeasures();
        assertNotEquals(0, s.size());
        Countermeasure cm = s.stream().findFirst().get();
        assertEquals(1, cm.getCountermeasureEvents().size());
        assertEquals(1, cm.getErrorCodes().size());
        assertEquals(1, cm.getActionCodes().size());
    }

    @Test
    public void testConverter() {
        User user = session.get(User.class, 1);
        assertNotNull(user);
        HibernateObjectPrinter.print(user);
        Bab b = session.get(Bab.class, 13091);
        assertNotNull(b);
        HibernateObjectPrinter.print(b);
    }
}
