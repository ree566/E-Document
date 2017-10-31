/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.CronTrigMod;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class ClearPollingJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(ClearPollingJob.class);
    
    @Autowired
    private CronTrigMod ctm;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            this.clearPollingJob("NumLamp", "Cell", "SensorCheck");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    private void clearPollingJob(String... jobGroupNames) throws SchedulerException {
        for (String jobGroupName : jobGroupNames) {
            ctm.removeTriggers(jobGroupName);
            ctm.removeJobs(jobGroupName);
        }
    }
}
