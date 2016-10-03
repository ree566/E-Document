/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 幫忙把BAB資料在晚上10點時記錄到資料庫(晚上6點的job 只關閉燈號 txt 1 -> 0)
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.entity.FBN;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.FBNService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BABDataSaver implements Job {

    private static final Logger log = LoggerFactory.getLogger(BABDataSaver.class);
    private final int LAST_HOUR_OF_DAY = 22;
    private final int MAX_WAIT_HOURS = 2;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        saveBABData();
    }

    //Auto save the data to Linebalancing_Main if user is not close the 工單.
    private void saveBABData() {

        List<BAB> unClosedBabs = this.getUnclosedBabs();
        log.info("Unclosed babList size = " + unClosedBabs.size());

        for (BAB bab : unClosedBabs) {
            log.info("Begin save unclose bab " + new Gson().toJson(bab));
            log.info("Close bab status :" + (BasicService.getBabService().closeBABWithoutCheckPrevSensor(bab) ? "success" : "fail"));
        }

    }

    private List getUnclosedBabs() {
        BABService babService = BasicService.getBabService();
        FBNService fbnService = BasicService.getFbnService();
        DateTime dt = new DateTime();
        int currentHour = dt.getHourOfDay();
        List<BAB> unClosedBabs;
        if (currentHour != LAST_HOUR_OF_DAY) {
            List<BAB> l = babService.getTimeOutBAB();
            unClosedBabs = new ArrayList();
            for (BAB bab : l) {
                FBN fbn = fbnService.getBABFinalStationSensorStatus(bab.getId());
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

    public static void main(String[] arg0) {

    }

}
