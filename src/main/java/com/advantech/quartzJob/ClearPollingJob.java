/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.CronTrigMod;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class ClearPollingJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ClearPollingJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            this.clearPollingJob("NumLamp", "Cell", "SensorCheck");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    private void clearPollingJob(String... jobGroupNames) throws SchedulerException {
        CronTrigMod mod = CronTrigMod.getInstance();
        for (String jobGroupName : jobGroupNames) {
            mod.removeTriggers(jobGroupName);
            mod.removeJobs(jobGroupName);
        }
    }
}
