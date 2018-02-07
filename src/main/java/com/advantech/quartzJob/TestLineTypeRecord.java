/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.model.Test;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.TestRecordService;
import com.advantech.service.TestService;
import com.advantech.webservice.WebServiceRV;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class TestLineTypeRecord extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(TestLineTypeRecord.class);
    private final int EXCLUDE_HOUR = 12;

    private final TestService testService;
    private final TestRecordService testRecordService;
    private WebServiceRV rv;

    public TestLineTypeRecord() {
        testService = (TestService) ApplicationContextHelper.getBean("testService");
        testRecordService = (TestRecordService) ApplicationContextHelper.getBean("testRecordService");
        rv = (WebServiceRV) ApplicationContextHelper.getBean("webServiceRV");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        DateTime d = new DateTime();
        log.info("It's " + d.toString() + " right now, begin record the testLineType...");
        if (d.getHourOfDay() == EXCLUDE_HOUR) {
            log.info("No need to record right now.");
        } else {
            //只存下已經刷入的使用者
            List<com.advantech.model.TestRecord> testLineTypeStatus = separateOfflineUser(rv.getTestLineTypeRecords());
            testRecordService.insert(testLineTypeStatus);
            log.info("Record success");
        }
    }

    private List<com.advantech.model.TestRecord> separateOfflineUser(List<com.advantech.model.TestRecord> l) {
        List<Test> tests = testService.findAll();
        List list = new ArrayList();
        Date d = new Date();
        l.forEach((user) -> {
            tests.stream().filter((t) -> (Objects.equals(user.getUserId(), t.getUserId()))).forEachOrdered((_item) -> {
                user.setLastUpdateTime(d);
                user.setTestTable(_item.getTestTable());
                list.add(user);
            });
        });
        return list;
    }

}
