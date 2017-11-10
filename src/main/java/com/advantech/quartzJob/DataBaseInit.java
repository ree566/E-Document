/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 把燈號顯示兩個主要的txt裏頭的參數由1(開燈)轉0(關燈)用
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.CellLineService;
import com.advantech.service.CellLineTypeFacade;
import com.advantech.service.LineService;
import com.advantech.service.TestLineTypeFacade;
import com.advantech.service.TestService;
import java.io.IOException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class DataBaseInit extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(DataBaseInit.class);

    private final TestService testService;
    private final LineService lineService;
    private final CellLineService cellLineService;
    private final BabLineTypeFacade bF;
    private final TestLineTypeFacade tF;
    private final CellLineTypeFacade cF;

    public DataBaseInit() {
        testService = (TestService) ApplicationContextHelper.getBean("testService");
        lineService = (LineService) ApplicationContextHelper.getBean("lineService");
        cellLineService = (CellLineService) ApplicationContextHelper.getBean("cellLineService");
        tF = (TestLineTypeFacade) ApplicationContextHelper.getBean("testLineTypeFacade");
        bF = (BabLineTypeFacade) ApplicationContextHelper.getBean("babLineTypeFacade");
        cF = (CellLineTypeFacade) ApplicationContextHelper.getBean("cellLineTypeFacade");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        dataInitialize();
    }

    private void dataInitialize() {
        try {
            testService.cleanTests();
            lineService.closeAllLine();
            cellLineService.closeAll();
            bF.resetAlarm();
            tF.resetAlarm();
            cF.resetAlarm();
            log.info("Data has been initialized.");
        } catch (IOException ex) {
            log.error("Data initialized fail because: " + ex);
        }

    }
}
