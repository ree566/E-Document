/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.BabPcsDetailHistoryDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.joda.time.DateTime;
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
public class TestSqlView {

    @Autowired
    private SqlViewDAO sqlViewDAO;
    
    @Autowired
    private BabPcsDetailHistoryDAO babPcsDetailHistoryDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void testFindBabLastGroupStatus() throws JsonProcessingException {
        List l = sqlViewDAO.findSensorCurrentGroupStatus(11240);
        assertEquals(2, l.size());
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testGetBabDetail() {
        DateTime sD = new DateTime(2017, 12, 01, 0, 0, 0, 0);
        DateTime eD = new DateTime(2017, 12, 31, 0, 0, 0, 0);
        List l = sqlViewDAO.findBabDetail("ASSY", "5", sD, eD, false);
        assertNotEquals(0, l.size());
        System.out.println(l.size());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testBabPcsDetailHistory() {
        List l = babPcsDetailHistoryDAO.findByBabForMap(14223);
        HibernateObjectPrinter.print(l);
    }
}
