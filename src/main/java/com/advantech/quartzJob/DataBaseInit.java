/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 把燈號顯示兩個主要的txt裏頭的參數由1(開燈)轉0(關燈)用
 */
package com.advantech.quartzJob;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.BasicService;
import com.advantech.service.TestLineTypeFacade;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Wei.Cheng
 */
public class DataBaseInit implements Job {
    
    private static final Logger log = LoggerFactory.getLogger(DataBaseInit.class);
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        dataInitialize();
    }
    
    private void dataInitialize() {
        try {
            BasicService.getTestService().cleanTestTable();
            BasicService.getLineService().closeAllLine();
            BabLineTypeFacade.getInstance().resetAlarm();
            TestLineTypeFacade.getInstance().resetAlarm();
            log.info("Data has been initialized.");
        } catch (IOException ex) {
            log.error("Data initialized fail because: " + ex);
        }
        
    }
}
