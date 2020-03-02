/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Bab;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.CronTrigMod;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng Detect the bab begin and end perLine
 */
public abstract class ProcessingBabDetector extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(ProcessingBabDetector.class);

    public static final String TEMP_BAB_KEYS = "tempBab";
    public static final String PROCESS_STATUS_KEY = "processStatus";
    public static final String STORE_JOB_KEYS = "storeKeys";

    private JSONObject processStatus = null;
    private List<Bab> tempBab = null;
    private Map<String, Map<String, Key>> storeKeys = null;

    private final String quartzJobNameExt;
    private final String quartzJobGroupName;
    private final String quartzJobCronTrigger;
    private final Class scheduleClass;

    private CronTrigMod ctm;

    protected ProcessingBabDetector(String quartzJobNameExt, String quartzJobGroupName, String quartzJobCronTrigger, Class scheduleClass) {
        this.quartzJobNameExt = quartzJobNameExt;
        this.quartzJobGroupName = quartzJobGroupName;
        this.quartzJobCronTrigger = quartzJobCronTrigger;
        this.scheduleClass = scheduleClass;
        ctm = (CronTrigMod) ApplicationContextHelper.getBean("cronTrigMod");
    }

    public void setCurrentStatus(JobDataMap jobMap) {
        tempBab = jobMap.get(TEMP_BAB_KEYS) == null ? null : (List<Bab>) jobMap.get(TEMP_BAB_KEYS);
        processStatus = jobMap.get(PROCESS_STATUS_KEY) == null ? new JSONObject() : (JSONObject) jobMap.get(PROCESS_STATUS_KEY);
        storeKeys = jobMap.get(STORE_JOB_KEYS) == null ? new HashMap<>() : (Map<String, Map<String, Key>>) jobMap.get(STORE_JOB_KEYS);
    }

    public abstract List<Bab> getProcessingBab();

    public void listeningBab() {
        List<Bab> processingBab = getProcessingBab();
        if (tempBab == null) {
            tempBab = processingBab;
            schedulePollingJob(processingBab);
        } else if (processingBab.size() != tempBab.size() || !processingBab.containsAll(tempBab)) {
            List<Bab> different = (List<Bab>) CollectionUtils.disjunction(processingBab, tempBab);
            different.forEach((b) -> {
                if (tempBab.contains(b)) {
                    String lineName = b.getLine().getName();
                    this.unschedulePollingJob(lineName);
                    removeBabFromTempList(b);
                    processStatus.remove(lineName);
                    storeKeys.remove(lineName);
                } else if (processingBab.contains(b)) {
                    tempBab.add(b);
                }
            });
            this.schedulePollingJob(tempBab);
        }

    }

    public void setStausIntoMap(JobDataMap jobMap) {
        //Retrive object status and update map in jobMap
        jobMap.put(TEMP_BAB_KEYS, this.tempBab);
        jobMap.put(PROCESS_STATUS_KEY, this.processStatus);
        jobMap.put(STORE_JOB_KEYS, this.storeKeys);
    }

    private void removeBabFromTempList(Bab bab) {
        Iterator it = tempBab.iterator();
        while (it.hasNext()) {
            Bab b = (Bab) it.next();
            if (b.equals(bab)) {
                it.remove();
            }
        }
    }

    private void schedulePollingJob(List<Bab> l) {
        l.forEach((b) -> {
            if (b.getIspre() == 1) {
                //return = continue
                return;
            }
            try {
                String lineName = b.getLine().getName();
                String jobName = lineName + quartzJobNameExt;
                JobKey jobKey = ctm.createJobKey(jobName, quartzJobGroupName);

                if (!ctm.isJobInScheduleExist(jobKey)) {
                    TriggerKey triggerKey = ctm.createTriggerKey(jobName, quartzJobGroupName);

                    Map babDetail = this.createJobDetails(b);

                    JobDetail jobDetail = ctm.createJobDetail(jobKey, quartzJobGroupName, this.scheduleClass, babDetail);

                    ctm.scheduleJob(jobDetail, triggerKey, quartzJobCronTrigger);

                    ctm.triggerJob(jobKey);

                    //put key in map when sche is success
                    Map keyMap = new HashMap();
                    keyMap.put("job", jobKey);
                    keyMap.put("trigger", triggerKey);
                    storeKeys.put(lineName, keyMap);
                }
            } catch (SchedulerException ex) {
                log.error(ex.toString());
            }
        });
    }

    public abstract Map createJobDetails(Bab b);

    private void unschedulePollingJob(String lineName) {
        try {
            Map keyMap = storeKeys.get(lineName);
            JobKey jobKey = (JobKey) keyMap.get("job");
            ctm.removeJob(jobKey);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public abstract JSONObject getProcessStatus();
}
