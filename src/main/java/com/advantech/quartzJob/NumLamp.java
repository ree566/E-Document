/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.helper.CronTrigMod;
import com.advantech.model.BasicDAO;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.google.gson.Gson;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
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

    private static List<BAB> tempBab = null;

    public NumLamp() {
        this.babService = BasicService.getBabService();
        this.ctm = CronTrigMod.getInstance();
    }

    public static NumLamp getInstance() {
        return instance == null ? new NumLamp() : instance;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        List<BAB> processingBab = babService.getAssyProcessing();
        if (tempBab == null) {
            out.println("First bab scan...");
            tempBab = processingBab;
            schedulePollingJob(processingBab);
        } else if (processingBab.size() != tempBab.size() || !processingBab.containsAll(tempBab)) {
            out.println("List is different...");
            List<BAB> different = this.getDifferent(processingBab, tempBab);
            for (BAB b : different) {
                if (tempBab.contains(b)) {
                    this.unschedulePollingJob(b.getLineName());
                    out.println("This job is closed, unsched it. " + b.getId());
                    tempBab.remove(b);
                    NUMLAMP_STATUS.remove(b.getLineName());
                } else if (processingBab.contains(b)) {
                    tempBab.add(b);
                    out.println("This job is new, sched it. " + b.getId());
                }
            }
            this.schedulePollingJob(tempBab);
        } else {
            out.println("List is not different, waiting next trigger time...");
        }

    }

    public void schedulePollingJob(List<BAB> l) {
        for (BAB b : l) {
            try {
                out.println("Sched job for line " + b.getLineName() + " , id: " + b.getId());
                String jobName = b.getLineName() + quartzNameExt;
                Map m = new HashMap();
                m.put("dataMap", b);
                JobDetail jobDetail = ctm.createJobDetail(jobName, LineBalancePeopleGenerator.class, m);
                ctm.generateAJob(jobDetail, ctm.createTriggerKey(jobName), "0/30 * 8-20 ? * MON-SAT *");
            } catch (SchedulerException ex) {
                log.error(ex.toString());
            }
        }
    }

    public void unschedulePollingJob(String lineName) {
        try {
            String jobName = lineName + quartzNameExt;
            out.println("Unsched job on line " + lineName);
            ctm.removeAJob(jobName);
            out.println(!ctm.isKeyInScheduleExist(jobName) ? "Job is success unsched.":"Job unsched fail");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public static JSONObject getNumLampStatus() {
        return NUMLAMP_STATUS;
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        BABService bService = BasicService.getBabService();
        List<BAB> listOne = bService.getAssyProcessing();
        List<BAB> listTwo = bService.getBABIdForCaculate();

        List<BAB> different = NumLamp.getInstance().getDifferent(listOne, listTwo);

        out.printf("One:%s%nTwo:%s%nDifferent:%s%n", new Gson().toJson(listOne), new Gson().toJson(listTwo), new Gson().toJson(different));
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
