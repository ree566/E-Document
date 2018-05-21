/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得資料庫資訊用
 */
package com.advantech.quartzJob;

import com.advantech.endpoint.Endpoint4;
import com.advantech.facade.FqcLineTypeFacade;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class PollingFqcResult implements EndpointPollingJob {

    private static final Logger log = LoggerFactory.getLogger(PollingFqcResult.class);

    @Autowired
    private FqcLineTypeFacade cF;

    @Autowired
    private Endpoint4 socket4;

    @Override
    public void dataBrocast() {
        /*
         ERROR - java.sql.SQLException: 
         Transaction (Process ID 69) was deadlocked on lock resources with another process 
         and has been chosen as the deadlock victim. Rerun the transaction. 
         Query: select * from LS_GetSenRealTime Parameters: []
         */
        try {
            socket4.sendAll(getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getData() {
        return new JSONArray().put(cF.getJSONObject()).toString();
    }
}
