/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.entity.LineOwnerMapping;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BABService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng Detect the bab begin and end perLine
 * http://ifeve.com/quartz-tutorial-job-jobdetail/
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class SensorDetect extends ProcessingBabDetector implements Job {

    public static JobDataMap jobDataMap = null;
    private Integer SENSOR_EXPIRE_TIME;
    private Integer SENSOR_DETECT_PERIOD;

    @Autowired
    private LineOwnerMappingService ownerService;

    @Autowired
    private BABService babService;

    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        SENSOR_EXPIRE_TIME = p.getSensorDetectExpireTime();
        SENSOR_DETECT_PERIOD = p.getSensorDetectPeriod();
        super.init(
                "_SensorCheck",
                "SensorCheck",
                "0 " + getMinutePeriodTime() + "/" + SENSOR_DETECT_PERIOD + " 8-11,13-20 ? * MON-SAT *",
                CheckSensor.class
        );
    }

    private Integer getMinutePeriodTime() {
        return new DateTime().getMinuteOfHour() % SENSOR_DETECT_PERIOD;
    }

    @Override
    public Map createJobDetails(BAB b) {
        Map m = new HashMap();
        m.put("bab", b);
        m.put("expireTime", SENSOR_EXPIRE_TIME);
        m.put("detectPeriod", SENSOR_DETECT_PERIOD);
        m.put("responsors", this.getResponsors(b));
        return m;
    }

    private JSONArray getResponsors(BAB b) {
        JSONArray arr = new JSONArray();
        List<LineOwnerMapping> responsors = ownerService.getByLine(b.getLine());
        for (LineOwnerMapping owner : responsors) {
            arr.put(owner.getUser_name());
        }
        return arr;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
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
    public List<BAB> getProcessingBab() {
        List<BAB> l = babService.getAllProcessing();
        return removePreBab(l);
    }

    private List<BAB> removePreBab(List<BAB> l) {
        Iterator it = l.iterator();
        while (it.hasNext()) {
            BAB b = (BAB) it.next();
            if (b.getIspre() == 1) {
                it.remove();
            }
        }

        return l;
    }
}
