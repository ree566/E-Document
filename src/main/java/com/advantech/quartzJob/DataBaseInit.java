/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 把燈號顯示兩個主要的txt裏頭的參數由1(開燈)轉0(關燈)用
 */
package com.advantech.quartzJob;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.CellLineService;
import com.advantech.service.CellLineTypeFacade;
import com.advantech.service.LineService;
import com.advantech.service.TestLineTypeFacade;
import com.advantech.service.TestService;
import java.io.IOException;
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
public class DataBaseInit implements Job {
    
    private static final Logger log = LoggerFactory.getLogger(DataBaseInit.class);
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private LineService lineService;
    
    @Autowired
    private CellLineService cellLineService;
    
    @Autowired
    private BabLineTypeFacade bf;
    
    @Autowired
    private TestLineTypeFacade tf;
    
    @Autowired
    private CellLineTypeFacade cf;
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        dataInitialize();
    }
    
    private void dataInitialize() {
        try {
            testService.cleanTestTable();
            lineService.closeAllLine();
            cellLineService.closeAll();
            bf.resetAlarm();
            tf.resetAlarm();
            cf.resetAlarm();
            log.info("Data has been initialized.");
        } catch (IOException ex) {
            log.error("Data initialized fail because: " + ex);
        }
        
    }
}
