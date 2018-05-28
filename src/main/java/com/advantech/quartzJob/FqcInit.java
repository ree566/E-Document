/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 把燈號顯示兩個主要的txt裏頭的參數由1(開燈)轉0(關燈)用
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.model.FqcLoginRecord;
import com.advantech.service.FqcLoginRecordService;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class FqcInit extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(FqcInit.class);

    private final FqcLoginRecordService fqcLoginRecordService;

    public FqcInit() {
        fqcLoginRecordService = (FqcLoginRecordService) ApplicationContextHelper.getBean("fqcLoginRecordService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        dataInitialize();
    }

    private void dataInitialize() {
        List<FqcLoginRecord> l = fqcLoginRecordService.findAll();
        l.forEach((f) -> {
            fqcLoginRecordService.delete(f);
        });
        log.info("Data has been initialized.");
    }
}
