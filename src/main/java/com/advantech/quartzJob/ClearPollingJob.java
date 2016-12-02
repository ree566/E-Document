/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.CronTrigMod;
import static java.lang.System.out;
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
public class ClearPollingJob implements Job {
    
    private static final Logger log = LoggerFactory.getLogger(ClearPollingJob.class);
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            out.println("clearing...");
            this.clearAllPollingJob();
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }
    
    private void clearAllPollingJob() throws SchedulerException {
        CronTrigMod mod = CronTrigMod.getInstance();
        mod.removeTriggers("NumLamp");
        mod.removeJobs("NumLamp");

        mod.removeTriggers("Cell");
        mod.removeJobs("Cell");
        
    }
}
