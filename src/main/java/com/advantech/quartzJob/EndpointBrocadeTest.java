/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 幫忙把BAB資料在晚上10點時記錄到資料庫(晚上6點的job 只關閉燈號 txt 1 -> 0)
 */
package com.advantech.quartzJob;

import com.advantech.endpoint.Endpoint;
import com.advantech.helper.ApplicationContextHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Endpoint testing class, can remove after system publish
 */
public class EndpointBrocadeTest extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(EndpointBrocadeTest.class);

    private final Endpoint socket;

    public EndpointBrocadeTest() {
        socket = (Endpoint) ApplicationContextHelper.getBean("endpoint");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        sendData();
    }

    private void sendData() {
        socket.sendAll("This is a testing brocade message...");
    }

}
