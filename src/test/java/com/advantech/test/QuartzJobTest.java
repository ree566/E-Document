/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.quartzJob.StandardTimeUpload1;
import com.advantech.quartzJob.WorktimeEventLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class QuartzJobTest {

    @Autowired
    @Qualifier("worktimeEventLog")
    private WorktimeEventLog job1;

    @Autowired
    @Qualifier("standardTimeUpload1")
    private StandardTimeUpload1 job2;

//    @Test
    public void testWorktimeEventLog() {
        job1.execute();
    }

    @Test
    public void testStandardTimeUpload() {

        job2.uploadToMes();
    }

}
