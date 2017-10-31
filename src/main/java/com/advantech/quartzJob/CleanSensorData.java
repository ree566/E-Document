/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.service.FBNService;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
//Clean the data in FBN table before target date.
public class CleanSensorData extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(CleanSensorData.class);
    private final DatetimeGenerator dg = new DatetimeGenerator("yyyy-MM-dd");
    private static final int SPECIFY_DAY = 7;
    
    @Autowired
    private FBNService fbnService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        DateTime d = new DateTime();
        d = d.minusDays(SPECIFY_DAY);
        String date = dg.dateFormatToString(d);
        fbnService.sensorDataClean(date);
    }
}
