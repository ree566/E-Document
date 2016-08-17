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

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        log.info("It's " + new Date() + " right now, begin record the testLineType...");
        TestService tService = BasicService.getTestService();
        List testLineTypeStatus = WebServiceRV.getInstance().getKanbantestUsers();
        boolean recordStatus = tService.recordTestLineType(testLineTypeStatus);
        log.info("Record status : " + recordStatus);
    }
}
