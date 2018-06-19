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
import com.advantech.model.Fqc;
import com.advantech.model.FqcLine;
import com.advantech.model.FqcLoginRecord;
import com.advantech.model.FqcModelStandardTime;
import com.advantech.model.FqcSettingHistory;
import com.advantech.model.Line;
import com.advantech.model.LineType;
import com.advantech.model.ModelSopRemark;
import com.advantech.model.TestTable;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.webservice.Factory;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
//@Rollback(true)
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

//    @Test
    public void testConverter() {
        User user = session.get(User.class, 1);
        assertNotNull(user);
        HibernateObjectPrinter.print(user);
        Bab b = session.get(Bab.class, 13091);
        assertNotNull(b);
        HibernateObjectPrinter.print(b);
    }

    @Test
    public void testFqcConverter() {
//        FqcLine fqcLine = session.get(FqcLine.class, 1);
//        assertNotNull(fqcLine);
//        assertEquals(Factory.DEFAULT, fqcLine.getFactory());
        
        List l = session.createCriteria(FqcLine.class).add(Restrictions.eq("factory", Factory.TEMP1)).list();
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);
    }

//    @Test
//    @Rollback(false)
    public void testLogin() {
        FqcLine fqcLine = session.get(FqcLine.class, 1);
        FqcLoginRecord loginRecord = new FqcLoginRecord(fqcLine, "A-Test");
        session.save(loginRecord);
        assertTrue(loginRecord.getId() != 0);
        System.out.println("Login record's id: " + loginRecord.getId());
        
        Fqc fqc = session.get(Fqc.class, 1);
        assertNotNull(fqc);
        FqcSettingHistory history = new FqcSettingHistory(fqc, "A-Test");
        session.save(history);
        assertTrue(history.getId() != 0);
        System.out.println("Setting history's id: " + history.getId());
    }

//    @Test
    @Rollback(true)
    public void testRecordCheck() {
        List<FqcLoginRecord> l = session
                .createCriteria(FqcLoginRecord.class).list();
        
        FqcLine line = session.get(FqcLine.class, 1);
        
        FqcLoginRecord pojo = new FqcLoginRecord(line, "A-8888");
        
        FqcLoginRecord existRecord = l.stream()
                .filter(p -> Objects.equals(p.getJobnumber(), pojo.getJobnumber())
                || Objects.equals(p.getFqcLine(), pojo.getFqcLine()))
                .findFirst().orElse(null);
        
        assertNotNull(existRecord);
        
        HibernateObjectPrinter.print(existRecord);
        
        checkArgument(existRecord == null, "Jobnumber or FqcLine is already in fqcRecord.");
    }

//    @Test
//    @Rollback(false)
    public void testModelSopRemark() {
        ModelSopRemark remark = session.get(ModelSopRemark.class, 1);
        assertNotNull(remark);
        
        Line line = session.get(Line.class, 1);
        assertNotNull(line);
        
        remark.setLines(newHashSet(line));
        session.save(remark);
    }

//    @Test
    @Rollback(false)
    public void testFqcModelStandardTime() {
        FqcModelStandardTime standardTime = new FqcModelStandardTime();
        standardTime.setModelNameCategory("BO");
        standardTime.setStandardTime(132);
        session.save(standardTime);
        assertTrue(standardTime.getId() != 0);
    }

//    @Test
//    @Rollback(true)
    public void testModelSopRemarkRelation() {
        String lineName = "LD";
        List<ModelSopRemark> l = session
                .createCriteria(ModelSopRemark.class)
                .createAlias("lines", "l")
                .createAlias("l.users", "u")
                .add(Restrictions.eq("u.jobnumber", "A-3813"))
                .list();
        
        assertTrue(!l.isEmpty());
        
        HibernateObjectPrinter.print(l);
    }

}
