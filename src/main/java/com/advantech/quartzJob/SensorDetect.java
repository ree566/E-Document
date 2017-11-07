/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.Bab;
import com.advantech.entity.LineOwnerMapping;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BabService;
import com.advantech.service.LineOwnerMappingService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Wei.Cheng Detect the bab begin and end perLine
 * http://ifeve.com/quartz-tutorial-job-jobdetail/
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SensorDetect extends ProcessingBabDetector {

    public static JobDataMap jobDataMap = null;
    private static final Integer SENSOR_EXPIRE_TIME;
    private static final Integer SENSOR_DETECT_PERIOD;

    private final LineOwnerMappingService lineOwnerMappingService;

    private final BabService babService;

    static {
        PropertiesReader p = PropertiesReader.getInstance();
        SENSOR_EXPIRE_TIME = p.getSensorDetectExpireTime();
        SENSOR_DETECT_PERIOD = p.getSensorDetectPeriod();
    }

    public SensorDetect() {
        super(
                "_SensorCheck",
                "SensorCheck",
                "0 " + getMinutePeriodTime() + "/" + SENSOR_DETECT_PERIOD + " 8-11,13-20 ? * MON-SAT *",
                CheckSensor.class
        );
        lineOwnerMappingService = (LineOwnerMappingService) ApplicationContextHelper.getBean("lineOwnerMappingService");
        babService = (BabService) ApplicationContextHelper.getBean("babService");
    }

    private static Integer getMinutePeriodTime() {
        return new DateTime().getMinuteOfHour() % SENSOR_DETECT_PERIOD;
    }

    @Override
    public Map createJobDetails(Bab b) {
        Map m = new HashMap();
        m.put("bab", b);
        m.put("expireTime", SENSOR_EXPIRE_TIME);
        m.put("detectPeriod", SENSOR_DETECT_PERIOD);
        m.put("responsors", this.getResponsors(b));
        return m;
    }

    private JSONArray getResponsors(Bab b) {
        JSONArray arr = new JSONArray();
        List<LineOwnerMapping> responsors = lineOwnerMappingService.getByLine(b.getLine());
        for (LineOwnerMapping owner : responsors) {
            arr.put(owner.getUser_name());
        }
        return arr;
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap jobMap = jec.getJobDetail().getJobDataMap();
        jobDataMap = jobMap;
        super.setCurrentStatus(jobMap);
        super.listeningBab(); //Process and get data
        super.setStausIntoMap(jobMap);
    }

    @Override
    public JSONObject getProcessStatus() {
        return jobDataMap == null ? new JSONObject() : (JSONObject) jobDataMap.get(PROCESS_STATUS_KEY);
    }

    @Override
    public List<Bab> getProcessingBab() {
        List<Bab> l = babService.getAllProcessing();
        return removePreBab(l);
    }

    private List<Bab> removePreBab(List<Bab> l) {
        Iterator it = l.iterator();
        while (it.hasNext()) {
            Bab b = (Bab) it.next();
            if (b.getIspre() == 1) {
                it.remove();
            }
        }

        return l;
    }
}
