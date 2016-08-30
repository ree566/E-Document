/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.quartzJob.DailyJobWorker;
import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.BasicLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.SimpleScheduleBuilder;
import static org.quartz.TriggerKey.triggerKey;

/**
 *
 * @author Wei.Cheng
 */
public class CronTrigMod {

    private static final Logger log = LoggerFactory.getLogger(DailyJobWorker.class);

    public static Map changedJobKey = new HashMap();

    private final String mainJobKey = "DailyJobWorker";

    private Scheduler scheduler;
    private static CronTrigMod instance;

    private CronTrigMod() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

//    http://openhome.cc/Gossip/DesignPattern/SingletonPattern.htm
    @SuppressWarnings("DoubleCheckedLocking")
    public static CronTrigMod getInstance() {
        if (instance == null) {
            instance = new CronTrigMod();
        }
        return instance;
    }

    public String getTriggerByJobKey(String jobKey) {
        try {
            Trigger t = scheduler.getTrigger(new TriggerKey(jobKey));
            return ((CronTrigger) t).getCronExpression();
        } catch (SchedulerException ex) {
            log.error(ex.toString());
            return null;
        }
    }

    public boolean triggerPauseOrResume(String order) {
        TxtWriter t = TxtWriter.getInstance();
        try {
            if ("pause".equals(order)) {
                scheduler.standby();
                t.turnAllSignToOpen();
                log.info("Stop write txt function");
            } else {
                t.resetAllTxt();
                log.info("Resume write txt function");
                scheduler.start();
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            return false;
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
        return true;
    }

    public void updateMainJobCronExpressionToDefault() throws SchedulerException {
        // retrieve the trigger
        Object trig = changedJobKey.get(mainJobKey);
        Trigger oldTrigger = (trig != null) ? (Trigger) trig : scheduler.getTrigger(triggerKey(mainJobKey));
        scheduler.rescheduleJob(oldTrigger.getKey(), oldTrigger);
        changedJobKey.remove(mainJobKey);
    }

    public void updateMainJobCronExpression() throws SchedulerException {
        // retrieve the trigger
        Object trig = changedJobKey.get(mainJobKey);

        Trigger oldTrigger = (trig != null) ? (Trigger) trig : scheduler.getTrigger(triggerKey(mainJobKey));
        changedJobKey.put(mainJobKey, oldTrigger);

        // obtain a builder that would produce the trigger
        TriggerBuilder tb = oldTrigger.getTriggerBuilder();

        // update the schedule associated with the builder, and build the new trigger
        // (other builder methods could be called, to change the trigger in any desired way)
        Trigger newTrigger = tb.withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(15)
                .withRepeatCount(1)
        ).build();

        scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
    }

    public boolean updateCronExpression(String triggerKey, String cronExpression, Integer executeNow) {
        if (StringUtils.isBlank(triggerKey) || StringUtils.isBlank(cronExpression)) {
            log.error("参数错误");
            return false;
        }
        if (executeNow == null) {
            executeNow = 0;
        }
        triggerKey = triggerKey.trim();
        cronExpression = cronExpression.trim();
        TriggerKey key = new TriggerKey(triggerKey);//动态条件  
        try {
            Trigger oldTrigger = scheduler.getTrigger(key);
            if (oldTrigger instanceof CronTriggerImpl) {
                CronTriggerImpl trigger = (CronTriggerImpl) oldTrigger;
                trigger.setCronExpression(cronExpression);//动态传入的条件  
                //不立即执行  
                if (executeNow == 0) {
                    trigger.setStartTime(new Date());//防止立即生效  
                }
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException | ParseException e) {
            log.error("更新cron定时任务运行时间失败[triggerKey=" + triggerKey + "]:", e);
            return false;
        }
        return true;
    }

    public void generateAJob(Class jobClass, JobKey jobKey, TriggerKey trigKey, String crontrigger) throws SchedulerException {
        if (scheduler.isStarted()) {
            if (!scheduler.checkExists(jobKey)) {
                JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(crontrigger)).build();
                scheduler.scheduleJob(job, trigger);
            }
        }
    }

    public void removeAJob(JobKey jobKey, TriggerKey trigKey) throws SchedulerException {
        if (scheduler.isStarted()) {
            scheduler.unscheduleJob(trigKey);
            scheduler.deleteJob(jobKey);
        }
    }

    public static void main(String arg0[]) {
        CronTrigger trigger = newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(cronSchedule("0 0/2 8-17 * * ?").withMisfireHandlingInstructionDoNothing())
                .forJob("myJob", "group1")
                .build();
    }
}
