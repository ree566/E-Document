/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BasicService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SensorDetect extends ProcessingBabDetector implements Job{
    
    public static JobDataMap jobDataMap = null;
 
    public SensorDetect() {
        super("_SensorCheck", "SensorCheck", "0 0/1 8-11,13-20 ? * MON-SAT *", CheckSensor.class);
    }

    @Override
    public Map createJobDetails(BAB b) {
        Map m = new HashMap();
        m.put("dataMap", b);
        m.put("expireTime", PropertiesReader.getInstance().getSensorDetectExpireTime());
        return m;
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
        return BasicService.getBabService().getAllProcessing();
    }
}
