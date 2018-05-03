/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.model.Test;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.model.LineType;
import com.advantech.model.LineTypeConfig;
import com.advantech.model.ReplyStatus;
import com.advantech.service.LineTypeConfigService;
import com.advantech.service.LineTypeService;
import com.advantech.service.TestRecordService;
import com.advantech.service.TestService;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.checkState;
import java.math.BigDecimal;
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
    private final LineTypeService lineTypeService;
    private final LineTypeConfigService lineTypeConfigService;
    private final WebServiceRV rv;
    
    private Double minProductivity, maxProductivity;

    public TestLineTypeRecord() {
        testService = (TestService) ApplicationContextHelper.getBean("testService");
        testRecordService = (TestRecordService) ApplicationContextHelper.getBean("testRecordService");
        rv = (WebServiceRV) ApplicationContextHelper.getBean("webServiceRV");
        lineTypeService = (LineTypeService) ApplicationContextHelper.getBean("lineTypeService");
        lineTypeConfigService = (LineTypeConfigService) ApplicationContextHelper.getBean("lineTypeConfigService");
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
            updateReplyFlag(testLineTypeStatus);
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

    private void updateReplyFlag(List<com.advantech.model.TestRecord> l) {
        initProductivityStandard();
        l.forEach((rec) -> {
            Double productivity = rec.getProductivity();
            rec.setReplyStatus(productivity > maxProductivity || productivity < minProductivity ? 
                    ReplyStatus.UNREPLIED : ReplyStatus.NO_NEED_TO_REPLY);
        });
    }

    private void initProductivityStandard() {
        LineType lineType = lineTypeService.findByName("Test");
        checkState(lineType != null, "Can't find lineType name Test.");
        List<LineTypeConfig> config = lineTypeConfigService.findByLineType(lineType.getId());
        LineTypeConfig minConf = config.stream().filter(s -> "PRODUCTIVITY_STANDARD_MIN".equals(s.getName())).findFirst().orElse(null);
        checkState(minConf.getValue() != null, "Can't find PRODUCTIVITY_STANDARD_MIN setting in lineTypeConfig.");
        this.minProductivity = minConf.getValue().doubleValue();
        LineTypeConfig maxConf = config.stream().filter(s -> "PRODUCTIVITY_STANDARD_MAX".equals(s.getName())).findFirst().orElse(null);
        checkState(maxConf.getValue() != null, "Can't find PRODUCTIVITY_STANDARD_MAX setting in lineTypeConfig.");
        this.maxProductivity = maxConf.getValue().doubleValue();
    }

}
