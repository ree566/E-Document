/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.helper.WebServiceRV;
import com.advantech.service.BasicService;
import com.advantech.service.TestService;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestLineTypeRecord implements Job {

    private static final Logger log = LoggerFactory.getLogger(TestLineTypeRecord.class);
    private final int EXCLUDE_HOUR = 12;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DateTime d = new DateTime();
        log.info("It's " + d.toString() + " right now, begin record the testLineType...");
        if (d.getHourOfDay() == EXCLUDE_HOUR) {
            log.info("No need to record right now.");
        } else {
            TestService tService = BasicService.getTestService();
            List testLineTypeStatus = WebServiceRV.getInstance().getKanbantestUser();
            boolean recordStatus = tService.recordTestLineType(testLineTypeStatus);
            log.info("Record status : " + recordStatus);
        }
    }
}
