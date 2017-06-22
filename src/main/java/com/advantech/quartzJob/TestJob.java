/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author Wei.Cheng
 */

@Configuration
@EnableAsync
//@EnableScheduling
public class TestJob {

    @Scheduled(cron="*/5 * * * * MON-FRI")
    public void echoCurrentTime() {
        System.out.println("Current time " + new DateTime());
    }
}
