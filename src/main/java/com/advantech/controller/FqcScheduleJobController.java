/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.controller;

import com.advantech.helper.CronTrigMod;
import com.advantech.model.Bab;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.Line;
import com.advantech.model.TagNameComparison;
import com.advantech.quartzJob.SyncPassStationRecord;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.TagNameComparisonService;
import static com.google.common.base.Preconditions.checkArgument;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import javax.servlet.http.*;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
//@Controller
@RequestMapping("/FqcScheduleJobController")
public class FqcScheduleJobController {

    private static final Logger log = LoggerFactory.getLogger(FqcScheduleJobController.class);

    private final String jobGroup = "fqc";
    private final String cronTrig = "0 0/1 8-20 ? * MON-SAT *";

    private Map<String, JobKey> schedJobs;

    @Autowired
    private BabService babService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;
    
    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private CronTrigMod ctm;

//    @PostConstruct
    public void init() {
        schedJobs = new HashMap();
        this.initProcessingBabs();
    }

    @RequestMapping(value = "/select", method = {RequestMethod.GET})
    @ResponseBody
    public List<JobKey> select() throws SchedulerException {
        return getSchedJobs();
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    public String insert(
            @RequestParam String po,
            @RequestParam String modelName,
            @RequestParam String tagName,
            @RequestParam String jobnumber,
            @RequestParam int firstPcsTimePeriod,
            HttpServletResponse res
    ) throws IOException, SchedulerException {

        BabSensorLoginRecord loginRecord = babSensorLoginRecordService.findBySensor(tagName);
        checkArgument(loginRecord != null, "Can't find login record on this tagName");
        checkArgument(Objects.equals(loginRecord.getJobnumber(), jobnumber), "Jobnumber is not matches at loginRecord in database");

        TagNameComparison tag = tagNameComparisonService.findByLampSysTagName(tagName);
        checkArgument(tag != null, "Can't find tagName " + tagName + " in setting.");

        Line line = tag.getLine();

        Bab b = new Bab(po, modelName, line, 1, 0);

        babService.checkAndInsert(b, tag);
        schedNewJobs(b, jobnumber);
        return "success";

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(
            @RequestParam String tagName,
            HttpServletResponse res
    ) throws IOException, SchedulerException {

        List<Bab> l = babService.findProcessingByTagName(tagName);
        checkArgument(!l.isEmpty(), "fail");

        Bab firstInput = l.get(0);
        babService.closeBab(firstInput);
        removeJob(firstInput);
        return "success";
    }

//    How to get jobnumber every fqc input?
    //Bab -> babSettingHistory -> getJobnumber
    private void initProcessingBabs() {
        List<Bab> l = babService.findProcessing();
        List<Bab> fqcProcessing = l.stream().filter(f -> "FQC".equals(f.getLine().getLineType().getName())).collect(toList());
        fqcProcessing.forEach((fqc) -> {
            try {
                List<BabSettingHistory> babSettings = babSettingHistoryService.findByBab(fqc);
                
                //FQC各紀錄只會有一個人生產
                BabSettingHistory setting = babSettings.get(0);
                this.schedNewJobs(fqc, setting.getJobnumber());
                
            } catch (SchedulerException ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

    private List<JobKey> getSchedJobs() throws SchedulerException {
        return ctm.getJobKeys("fqc");
    }

    private void schedNewJobs(Bab fqc, String jobnumber) throws SchedulerException {
        String jobKeyName = this.generatefqcJobKeyName(fqc);
        Map data = new HashMap();
        data.put("bab", fqc);
        data.put("jobnumber", jobnumber);
        JobKey jobKey = ctm.createJobKey(jobKeyName, jobGroup);
        TriggerKey triggerKey = ctm.createTriggerKey(jobKeyName, jobGroup);
        JobDetail detail = ctm.createJobDetail(jobKey, jobGroup, SyncPassStationRecord.class, data);
        ctm.scheduleJob(detail, triggerKey, cronTrig);
        schedJobs.put(Integer.toString(fqc.getId()), jobKey);
    }

    private void removeJob(Bab fqc) throws SchedulerException {
        String key = Integer.toString(fqc.getId());
        JobKey jobKey = schedJobs.get(key);
        if (jobKey == null) {
            String jobKeyName = this.generatefqcJobKeyName(fqc);
            jobKey = ctm.createJobKey(jobKeyName, this.jobGroup);
        }
        if (ctm.isJobInScheduleExist(jobKey)) {
            ctm.removeJob(jobKey);
        }
    }

    private String generatefqcJobKeyName(Bab fqc) {
        return fqc.getId() + "_" + fqc.getLine().getId() + "_" + fqc.getPo();
    }
}
