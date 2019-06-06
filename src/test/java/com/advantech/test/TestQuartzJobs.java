/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.quartzJob.BackupDataToExcel;
import com.advantech.quartzJob.StandardTimeUpload;
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
    private BackupDataToExcel backupExcel;
    
    @Autowired
    private StandardTimeUpload standardTimeUpload;

//    @Test
    public void testBackupDataToExcel() throws JobExecutionException, Exception {
        backupExcel.backupToDisk();
    }
    
    @Test
    public void testStandardTimeUpload() throws JobExecutionException, Exception {
        standardTimeUpload.uploadToMes();
    }

}
