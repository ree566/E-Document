/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.facade.BabLineTypeFacade;
import com.advantech.facade.TestLineTypeFacade;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class DailyJobWorker extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(DailyJobWorker.class);

    private final TestLineTypeFacade tF;

    private final BabLineTypeFacade bF;
    
    public DailyJobWorker(){
        tF = (TestLineTypeFacade) ApplicationContextHelper.getBean("testLineTypeFacade");
        bF = (BabLineTypeFacade) ApplicationContextHelper.getBean("babLineTypeFacade");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) {
        //處理測試和組包裝線別資料，並依照設定output to db or txt
        this.processingBabData();
        this.processingTestData();
    }

    private void processingTestData() {
        try {
            tF.processingDataAndSave();
        } catch (Exception ex) {
            log.error("TestLineTypeFacade throw some exception", ex);
        }
    }

    private void processingBabData() {
        try {
            bF.processingDataAndSave();
        } catch (Exception ex) {
            log.error("BabLineTypeFacade throw some exception", ex);
        }
    }

}
