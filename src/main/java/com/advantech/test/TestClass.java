/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;
import java.util.Date;
import static org.quartz.DateBuilder.evenMinuteDate;
import org.quartz.Job;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass implements Job {

    public static void main(String args[]) throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(TestClass.class)
                .withIdentity("job1", "group1")
                .build();

        Date runTime = evenMinuteDate(new Date());

// Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .build();
        
        sched.scheduleJob(job, trigger);
        
        sched.start();
        
        Thread.sleep(90L * 1000L);
        
        sched.shutdown(true);
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        out.println(("Hello World! - " + new Date()));
    }

}
