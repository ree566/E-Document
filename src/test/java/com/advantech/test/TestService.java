/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.Bab;
import com.advantech.model.BabSettingHistory;
import com.advantech.quartzJob.BabDataSaver;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.LineBalancingService;
import com.advantech.service.TagNameComparisonService;
import com.advantech.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class TestService {

    @Autowired
    private BabService babService;

    @Autowired
    private LineBalancingService lineBalancingService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private BabLineTypeFacade bf;

//    @Test
    @Transactional
    @Rollback(true)
    public void testLineBalancingService() throws JobExecutionException {
        new BabDataSaver().executeInternal(null);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSettingHistoryService() {

        BabSettingHistory setting2 = babSettingHistoryService.findProcessingByTagName("L8-S-3");
        assertNotNull(setting2);

        assertEquals(setting2.getTagName().getName(), "L8-S-3");
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSensorLoginRecordService() throws JsonProcessingException {

        List l = babSensorLoginRecordService.findByLine(3);
        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testBabSettingHistory() throws JsonProcessingException {

        List<BabSettingHistory> allSettings = babSettingHistoryService.findProcessing();
        Bab b = babService.findByPrimaryKey(12991);

        HibernateObjectPrinter.print(allSettings.get(0));

        List<BabSettingHistory> l = allSettings.stream()
                .filter(rec -> rec.getBab().getId() == b.getId()).collect(toList());

        HibernateObjectPrinter.print(l.get(0));

        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(allSettings);
        HibernateObjectPrinter.print(l);
    }

    @Test
    public void testHibernateInitialize() {
//        List<User> l = userService.findByUserNotification("sensor_alarm");
//
//        Object[] toMails = l.stream()
//                .map(User::getEmail).collect(Collectors.toList())
//                .stream().toArray(String[]::new);
//
//        HibernateObjectPrinter.print(toMails);

        
        HibernateObjectPrinter.print(lineBalancingService.findAll());
        HibernateObjectPrinter.print(bf.generateData());
    }

}
