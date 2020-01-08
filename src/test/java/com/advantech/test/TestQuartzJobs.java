/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.quartzJob.ArrangePrepareSchedule;
import com.advantech.quartzJob.CleanSensorData;
import com.advantech.quartzJob.CountermeasureAlarm;
import com.advantech.quartzJob.HandleUncloseBab;
import com.advantech.quartzJob.DataBaseInit;
import com.advantech.quartzJob.InsertMesCountRecord;
import com.advantech.quartzJob.PollingDataCollectStatus;
import com.advantech.quartzJob.SyncPrepareSchedule;
import com.advantech.quartzJob.SyncTestPassStationData;
import com.advantech.quartzJob.TestLineTypeRecord;
import com.advantech.quartzJob.TestLineTypeRecordUnrepliedAlarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PollingDataCollectStatus job;
    
    @Autowired
    private SyncTestPassStationData job2;

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

//    @Test
    public void testRecordUnrepliedAlarm() throws JobExecutionException {
        TestLineTypeRecordUnrepliedAlarm t = new TestLineTypeRecordUnrepliedAlarm();
        System.out.println(t.generateMailBody());
    }

//    @Test
    public void testPollingData() throws JobExecutionException {
        HibernateObjectPrinter.print(job.getData());
    }

//    @Test
    public void testInsertMesCountRecord() throws JobExecutionException {
        InsertMesCountRecord i = new InsertMesCountRecord();
        i.executeInternal(null);
    }
    
//    @Test
    public void testSyncTestPassStationData() throws JobExecutionException {
        job2.execute();
    }
    
    @Autowired
    private SyncPrepareSchedule sps;
    
    @Autowired
    private ArrangePrepareSchedule aps;
    
    @Test
    public void testSyncPrepareSchedule1() throws Exception {
        sps.execute();
        aps.execute();
    }
    
}
