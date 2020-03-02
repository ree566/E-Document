/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得資料庫資訊用
 */
package com.advantech.quartzJob;

import com.advantech.endpoint.Endpoint;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.db1.BabDataCollectMode;
import com.advantech.service.db1.FbnService;
import com.google.gson.Gson;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class PollingDataCollectStatus implements EndpointPollingJob {

    private static final Logger log = LoggerFactory.getLogger(PollingDataCollectStatus.class);

    private Gson gson;

    @Autowired
    private FbnService fbnService;

    @Autowired
    private Endpoint socket;

    @Autowired
    private PropertiesReader reader;

    private BabDataCollectMode mode;

    @PostConstruct
    public void init() {
        gson = new Gson();
        mode = reader.getBabDataCollectMode();
    }

    //抓取資料庫資料並廣播
    @Override
    public void dataBrocast() {
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
        switch (mode) {
            case AUTO:
                return gson.toJson(fbnService.getSensorCurrentStatus());
            case MANUAL:
                return gson.toJson(fbnService.getBarcodeCurrentStatus());
            default:
                return "{}";
        }
    }
}
