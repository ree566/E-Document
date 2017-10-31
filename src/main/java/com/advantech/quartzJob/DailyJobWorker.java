/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component(value = "DailyJobWorker")
public class DailyJobWorker {

    private static final Logger log = LoggerFactory.getLogger(DailyJobWorker.class);

    @Autowired
    private TestLineTypeFacade tF;

    @Autowired
    private BabLineTypeFacade bF;

    protected void execute() {
        //處理測試和組包裝線別資料，並依照設定output to db or txt
        System.out.println("Process daliy job");
        this.processingBabData();
        this.processingTestData();
        System.out.println("Process daliy finished"); 
    }

    private void processingTestData() {
        try {
            tF.processingDataAndSave();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
