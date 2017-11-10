/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得資料庫資訊用
 */
package com.advantech.quartzJob;

import com.advantech.endpoint.Endpoint2;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class PollingBabAndTestResult extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(PollingBabAndTestResult.class);

    private static TestLineTypeFacade tF;

    private static BabLineTypeFacade bF;

    static {
        tF = (TestLineTypeFacade) ApplicationContextHelper.getBean("testLineTypeFacade");
        bF = (BabLineTypeFacade) ApplicationContextHelper.getBean("babLineTypeFacade");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        dataBrocast();
    }

    //抓取資料庫資料並廣播
    private void dataBrocast() {
        /*
         ERROR - java.sql.SQLException: 
         Transaction (Process ID 69) was deadlocked on lock resources with another process 
         and has been chosen as the deadlock victim. Rerun the transaction. 
         Query: select * from LS_GetSenRealTime Parameters: []
         */
        try {
            System.out.println("PollingBabAndTestResult");
            Endpoint2.sendAll(getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getData() {
        System.out.println(new Gson().toJson(tF.getJSONObject()));
        return new JSONArray().put(tF.getJSONObject())
                .put(bF.getJSONObject()).toString();
    }
}
