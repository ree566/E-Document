/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得資料庫資訊用
 */
package com.advantech.quartzJob;

import com.advantech.endpoint.Endpoint;
import com.advantech.endpoint.Endpoint2;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.FbnService;
import com.google.gson.Gson;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class PollingSensorStatus extends QuartzJobBean {

    private static final Gson gson;
    private static final Logger log = LoggerFactory.getLogger(PollingSensorStatus.class);
    private static final FbnService fbnService;
    private static Endpoint socket;
    
    static{
        gson = new Gson();
        fbnService = (FbnService) ApplicationContextHelper.getBean("fbnService");
        socket = (Endpoint) ApplicationContextHelper.getBean("endpoint");
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
            socket.sendAll(getData());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public String getData() {
        return gson.toJson(fbnService.getSensorCurrentStatus());
    }
}
