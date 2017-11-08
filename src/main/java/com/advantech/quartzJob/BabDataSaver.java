/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 幫忙把BAB資料在晚上10點時記錄到資料庫(晚上6點的job 只關閉燈號 txt 1 -> 0)
 */
package com.advantech.quartzJob;

import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.model.Fbn;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.BabService;
import com.advantech.service.FbnService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
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
public class BabDataSaver extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(BabDataSaver.class);
    private final int LAST_HOUR_OF_DAY = 22;
    private final int MAX_WAIT_HOURS = 2;

    private final BabService babService;

    private final FbnService fbnService;

    public BabDataSaver() {
        babService = (BabService) ApplicationContextHelper.getBean("babService");
        fbnService = (FbnService) ApplicationContextHelper.getBean("fbnService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        saveBABData();
    }

    //Auto save the data to Linebalancing_Main if user is not close the 工單.
    private void saveBABData() {

        List<Bab> unClosedBabs = this.getUnclosedBabs();
        log.info("Unclosed babList size = " + unClosedBabs.size());

        for (Bab bab : unClosedBabs) {
            bab.setIsused(BabStatus.UNFINSHED.getValue());
            log.info("Begin save unclose bab " + new Gson().toJson(bab));
            log.info("Close bab status :" + (babService.closeBABWithoutCheckPrevSensor(bab) ? "success" : "fail"));
        }

    }

    private List getUnclosedBabs() {
        DateTime dt = new DateTime();
        int currentHour = dt.getHourOfDay();
        List<Bab> unClosedBabs;
        if (currentHour != LAST_HOUR_OF_DAY) {
            List<Bab> l = babService.getTimeOutBAB();
            unClosedBabs = new ArrayList();
            for (Bab bab : l) {
                Fbn fbn = fbnService.getBABFinalStationSensorStatus(bab.getId());
                if (fbn == null) {
                    continue;
                }
                int diffHours = fbnService.checkHoursDiff(fbn);
                if (diffHours >= MAX_WAIT_HOURS) {
                    unClosedBabs.add(bab);
                    log.info("Unclosed babList add " + new Gson().toJson(bab));
                }
            }
        } else {
            unClosedBabs = babService.getProcessingBAB();
        }

        return unClosedBabs;
    }

}
