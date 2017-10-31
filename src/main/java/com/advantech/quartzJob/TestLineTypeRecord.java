/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.entity.Test;
import com.advantech.entity.TestLineTypeUser;
import com.advantech.webservice.WebServiceRV;
import com.advantech.service.TestService;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class TestLineTypeRecord implements Job {

    private static final Logger log = LoggerFactory.getLogger(TestLineTypeRecord.class);
    private final int EXCLUDE_HOUR = 12;
    
    @Autowired
    private TestService tService;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DateTime d = new DateTime();
        log.info("It's " + d.toString() + " right now, begin record the testLineType...");
        if (d.getHourOfDay() == EXCLUDE_HOUR) {
            log.info("No need to record right now.");
        } else {
            //只存下已經刷入的使用者
            List<TestLineTypeUser> testLineTypeStatus = separateOfflineUser(WebServiceRV.getInstance().getTestLineTypeUsers());
            boolean recordStatus = tService.recordTestLineType(testLineTypeStatus);
            log.info("Record status : " + recordStatus);
        }
    }

    private List<TestLineTypeUser> separateOfflineUser(List<TestLineTypeUser> l) {
        List<Test> tables = tService.getAllTableInfo();
        List list = new ArrayList();
        for (TestLineTypeUser user : l) {
            for (Test t : tables) {
                if (user.getUserNo().equals(t.getUserid())) {
                    list.add(user);
                }
            }
        }
        return list;
    }

}
