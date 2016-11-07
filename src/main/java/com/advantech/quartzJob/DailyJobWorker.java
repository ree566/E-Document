/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.BasicLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class DailyJobWorker implements Job {

    private static final Logger log = LoggerFactory.getLogger(DailyJobWorker.class);
    private final BasicLineTypeFacade tF = TestLineTypeFacade.getInstance();
    private final BasicLineTypeFacade bF = BabLineTypeFacade.getInstance();

    public DailyJobWorker() {

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        //處理測試和組包裝線別資料，並依照設定output
        this.processingBabData();
        this.processingTestData();
    }

    private void processingTestData() {
        try {
            tF.processingDataAndSave();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    private void processingBabData() {
        try {
            bF.processingDataAndSave();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }
}
