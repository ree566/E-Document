/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.service.BABService;
import com.advantech.service.WorkTimeService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng Detect the bab begin and end perLine
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class NumLamp extends ProcessingBabDetector implements Job {

    public static JobDataMap jobDataMap = null;
    
    @Autowired
    private WorkTimeService workTimeService;
    
    @Autowired
    private BABService babService;

    @PostConstruct
    public void init() {
        super.init("_NumLamp", "NumLamp", "25 0/5 8-20 ? * MON-SAT *", LineBalancePeopleGenerator.class);
    }

    @Override
    public Map createJobDetails(BAB b) {
        Double testStandardTime = workTimeService.getTestStandardTime(b.getModel_name());
        Integer totalQuantity = babService.getPoTotalQuantity(b.getPO());
        Map m = new HashMap();
        m.put("bab", b);
        m.put("testStandardTime", testStandardTime);
        m.put("totalQuantity", totalQuantity);
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
        List<BAB> l = babService.getAssyProcessing();
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
