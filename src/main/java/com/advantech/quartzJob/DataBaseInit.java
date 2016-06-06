/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 把燈號顯示兩個主要的txt裏頭的參數由1(開燈)轉0(關燈)用
 */
package com.advantech.quartzJob;

import com.advantech.helper.PropertiesReader;
import com.advantech.helper.TxtWriter;
import com.advantech.service.BasicService;
import com.advantech.service.LineService;
import com.advantech.service.TestService;
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
    private static String testTxtPath;
    private static String babTxtPath;

    private final LineService lineService;
    private final TestService testService;

    public DataBaseInit() {
        PropertiesReader p = PropertiesReader.getInstance();
        testTxtPath = p.getTestTxtName();
        babTxtPath = p.getBabTxtName();
        lineService = BasicService.getLineService();
        testService = BasicService.getTestService();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        dataInitialize();
    }

    private void dataInitialize() {
        testService.cleanTestTable();
        lineService.closeAllLine();
        TxtWriter t = TxtWriter.getInstance();
        try {
            t.txtReset(testTxtPath);
            t.txtReset(babTxtPath);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        DataTransformer.initInnerObjs();
    }
}
