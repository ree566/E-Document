/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.service.BwFieldService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng
 */
public class BwFieldSyncJob {

    private static final Logger log = LoggerFactory.getLogger(BwFieldSyncJob.class);

    @Autowired
    private BwFieldService bwFieldService;

    //將藍燈field透過預存同步update回Sql
    public void syncBwData() {
        boolean updateFlag = bwFieldService.update() == 1;

        log.info("Auto update bwField at : " + new DateTime() + " --> " + updateFlag);
    }
}
