/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.quartzJob.CleanSensorData;
import com.advantech.quartzJob.CountermeasureAlarm;
import com.advantech.quartzJob.HandleUncloseBab;
import com.advantech.quartzJob.DataBaseInit;
import com.advantech.quartzJob.TestLineTypeRecord;
import com.advantech.quartzJob.TestLineTypeRecordUnrepliedAlarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
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
public class TestQuartzJobs {

//    @Test
    public void testTestLineTypeRecord() throws JobExecutionException {
        TestLineTypeRecord tr = new TestLineTypeRecord();
        tr.executeInternal(null);
    }

//    @Test
    public void testDbInit() throws JobExecutionException {
        DataBaseInit d = new DataBaseInit();
        d.executeInternal(null);
    }

//    @Test
    public void testBabDataSaver() throws JobExecutionException {
        HandleUncloseBab b = new HandleUncloseBab();
        b.executeInternal(null);
    }

//    @Test
    public void testCleanSensorData() throws JobExecutionException {
        CleanSensorData c = new CleanSensorData();
        c.executeInternal(null);
    }

//    @Test
    public void testCountermeasureAlarm() throws JobExecutionException {
        CountermeasureAlarm c = new CountermeasureAlarm();
        c.executeInternal(null);
    }

    @Test
    public void testRecordUnrepliedAlarm() throws JobExecutionException {
        TestLineTypeRecordUnrepliedAlarm t = new TestLineTypeRecordUnrepliedAlarm();
        System.out.println(t.generateMailBody());
    }
}
