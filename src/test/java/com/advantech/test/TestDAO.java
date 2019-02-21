/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.BabDAO;
import com.advantech.dao.BabPcsDetailHistoryDAO;
import com.advantech.dao.BabPreAssyPcsRecordDAO;
import com.advantech.dao.BabSettingHistoryDAO;
import com.advantech.dao.CountermeasureDAO;
import com.advantech.dao.FqcDAO;
import com.advantech.dao.SensorTransformDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.dao.TagNameComparisonDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabPreAssyPcsRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.Countermeasure;
import com.advantech.model.Line;
import com.advantech.model.MesLine;
import com.advantech.model.MesPassCountRecord;
import com.advantech.model.SensorTransform;
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
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
public class TestDAO {

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private BabSettingHistoryDAO babSettingHistoryDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BabPcsDetailHistoryDAO babPcsDetailHistoryDAO;

    @Autowired
    private SensorTransformDAO sensorTransformDAO;

    @Autowired
    private TagNameComparisonDAO tagDAO;

    @Autowired
    private CountermeasureDAO countermeasureDAO;

    @Autowired
    private FqcDAO fqcDAO;

    @Autowired
    private SqlViewDAO sqlViewDAO;

    @Autowired
    private BabPreAssyPcsRecordDAO prePcsRecDAO;

    @Autowired
    private WebServiceRV rv;

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabDAO() throws JsonProcessingException {

        for (int i = 1; i <= 4; i++) {
            List l = babDAO.findUnReplyed(i);
            assertTrue(l.isEmpty());
        }

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabDAO2() throws JsonProcessingException, ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date today = ft.parse("2017-12-21 00:00:00");
        BabSettingHistory setting = babSettingHistoryDAO.findByPrimaryKey(18893);
        assertNotNull(setting);
        setting.setJobnumber("A-7567");
        babSettingHistoryDAO.update(setting);
        assertNotNull(setting.getLastUpdateTime());
        assertTrue(setting.getLastUpdateTime().after(today));
        HibernateObjectPrinter.print(setting);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabPcsDetailHistoryDAO() throws JsonProcessingException, ParseException {
        List<Map> l = babPcsDetailHistoryDAO.findByBabForMap(12597);
        assertNotEquals(0, l.size());
        Map m = l.get(0);
        assertNotNull(m.get("id"));
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testHibernateInitialize() {
        Session session = sessionFactory.getCurrentSession();

        Line line = new Line();
        line.setId(1);

        Bab b = new Bab("test", "test", line, 3, 1);
        session.save(b);
        assertTrue(b.getId() != 0);

        HibernateObjectPrinter.print(b);

        session.flush();

        Hibernate.initialize(b.getLine());

        assertTrue(line.getName() != null);

        HibernateObjectPrinter.print(line);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFindWithBestBalanceAndSetting() {
        List l = babSettingHistoryDAO.findAll("PSH5579ZA", 0, true, false, 0);
        assertTrue(l != null);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testTagNameCompRange() {
        SensorTransform sensor = sensorTransformDAO.findByPrimaryKey("L4-S-1");
        HibernateObjectPrinter.print(this.tagDAO.findInRange(sensor, 5));
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testCountermeasure() {
        Countermeasure cm = countermeasureDAO.findByBabAndTypeName(23365, "Bab_Abnormal_LineProductivity");
        assertNotNull(cm);
        assertNotNull(cm.getCountermeasureType());
        assertEquals(2, cm.getCountermeasureType().getId());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFqc() {
        List l = fqcDAO.findProcessingWithLine();
        assertTrue(l.isEmpty());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlView() {
        DateTime eD = new DateTime();
        DateTime sD = eD.minusWeeks(1);
        List l = sqlViewDAO.findBabPassStationExceptionReport(null, null, sD, eD, 1);
//        assertTrue(l.isEmpty());
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testBabPreAssyPcsRecord() {
        BabSettingHistory history = babSettingHistoryDAO.findByPrimaryKey(1);
        assertNotNull(history);
        BabPreAssyPcsRecord pcsRec = new BabPreAssyPcsRecord(history, 50);
        prePcsRecDAO.insert(pcsRec);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testMesCountRecord() {
        DateTime eD = new DateTime();
        DateTime sD = eD.minusDays(30);

        Session session = sessionFactory.getCurrentSession();

        List<MesLine> lines = session.createCriteria(MesLine.class).list();
        List<MesPassCountRecord> existData = session.createCriteria(MesPassCountRecord.class).list();

        List<Integer> lineId = lines.stream().map(MesLine::getId).collect(toList());
        assertTrue(!lineId.isEmpty());

        List<MesPassCountRecord> l = rv.getMesPassCountRecords(sD, eD, Factory.DEFAULT);

        System.out.println(l.size());

        List<MesPassCountRecord> newData = l.stream()
                .filter(e -> lineId.contains(e.getMesLineId()) && !existData.contains(e))
                .collect(toList());
        
        System.out.println(newData.size());

        assertTrue(!newData.isEmpty());

        newData.forEach(e -> {
            e.setId(0);
            session.merge(e);
        });
    }

    private boolean isRecordExist(List<MesPassCountRecord> existData, MesPassCountRecord newData) {
        List<MesPassCountRecord> l = existData.stream()
                .filter(e -> e.equals(newData))
                .collect(toList());
        return !l.isEmpty();
    }

}
