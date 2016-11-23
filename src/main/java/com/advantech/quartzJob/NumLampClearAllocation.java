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

/**
 *
 * @author Wei.Cheng
 */
public class NumLampClearAllocation implements Job {
    
    private static final Logger log = LoggerFactory.getLogger(NumLampClearAllocation.class);
    
    private final String groupName = "NumLamp";
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            this.clearAllJobNumLampAllocation();
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }
    
    private void clearAllJobNumLampAllocation() throws SchedulerException {
        CronTrigMod mod = CronTrigMod.getInstance();
        mod.removeTriggers(groupName);
        mod.removeJobs(groupName);
    }
}
