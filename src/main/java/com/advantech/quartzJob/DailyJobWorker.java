/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Wei.Cheng
 */
public class DailyJobWorker implements Job {

    public DailyJobWorker() {

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DataTransformer.getInstance().doDailyJob();  
    }
}
