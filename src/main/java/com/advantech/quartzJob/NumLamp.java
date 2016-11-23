/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.helper.CronTrigMod;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class NumLamp implements Job {

    private static final Logger log = LoggerFactory.getLogger(NumLamp.class);
    private static final JSONObject NUMLAMP_STATUS = new JSONObject();

    private static NumLamp instance;

    private final BABService babService;
    private final CronTrigMod ctm;
    private final String quartzNameExt = "_NumLamp";
    private final String groupName = "NumLamp";

    private static List<BAB> tempBab = null;

    private static final Map<String, Map<String, Key>> STORE_KEYS = new HashMap();

    public NumLamp() {
        this.babService = BasicService.getBabService();
        this.ctm = CronTrigMod.getInstance();
    }

    public static NumLamp getInstance() {
        if (instance == null) {
            instance = new NumLamp();
        }
        return instance;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        List<BAB> processingBab = babService.getAssyProcessing();
        if (tempBab == null) {
            tempBab = processingBab;
            schedulePollingJob(processingBab);
        } else if (processingBab.size() != tempBab.size() || !processingBab.containsAll(tempBab)) {
            List<BAB> different = this.getDifferent(processingBab, tempBab);
            for (BAB b : different) {
                if (tempBab.contains(b)) {
                    this.unschedulePollingJob(b.getLineName());
                    removeBabFromTempList(b);
                    NUMLAMP_STATUS.remove(b.getLineName());
                    STORE_KEYS.remove(b.getLineName());
                } else if (processingBab.contains(b)) {
                    tempBab.add(b);
                }
            }
            this.schedulePollingJob(tempBab);
        } else {
        }

    }

    private void removeBabFromTempList(BAB bab) {
        Iterator it = tempBab.iterator();
        while (it.hasNext()) {
            BAB b = (BAB) it.next();
            if (b.equals(bab)) {
                it.remove();
            }
        }
    }

    public void schedulePollingJob(List<BAB> l) {
        for (BAB b : l) {
            try {
                String jobName = b.getLineName() + quartzNameExt;
                JobKey jobKey = ctm.createJobKey(jobName, groupName);

                if (!ctm.isJobInScheduleExist(jobKey)) {
                    TriggerKey triggerKey = ctm.createTriggerKey(jobName, groupName);

                    Map m = new HashMap();
                    m.put("dataMap", b);
                    JobDetail jobDetail = ctm.createJobDetail(jobKey, groupName, LineBalancePeopleGenerator.class, m);
                    ctm.scheduleJob(jobDetail, triggerKey, "0/30 * 8-20 ? * MON-SAT *");

                    //put key in map when sche is success
                    Map keyMap = new HashMap();
                    keyMap.put("job", jobKey);
                    keyMap.put("trigger", triggerKey);
                    STORE_KEYS.put(b.getLineName(), keyMap);
                }
            } catch (SchedulerException ex) {
                log.error(ex.toString());
            }
        }
    }

    public void unschedulePollingJob(String lineName) {
        try {
            String jobName = lineName + quartzNameExt;
            Map keyMap = STORE_KEYS.get(lineName);
            JobKey jobKey = (JobKey) keyMap.get("job");
            ctm.removeJob(jobKey);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public static JSONObject getNumLampStatus() {
        return NUMLAMP_STATUS;
    }

    public List<BAB> getDifferent(List<BAB> listOne, List<BAB> listTwo) {
        Collection<BAB> similar = new HashSet<>(listOne);
        Collection<BAB> different = new HashSet<>();
        different.addAll(listOne);
        different.addAll(listTwo);

        similar.retainAll(listTwo);
        different.removeAll(similar);

        return new ArrayList(different);
    }
}
