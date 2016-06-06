/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import java.util.HashMap;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SensorDBListener implements Job {

    private static final Logger log = LoggerFactory.getLogger(SensorDBListener.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

    }
    
    public static void main(String[] arg0){
        String str = "test";
        Map m = new HashMap();
        int i = (int)(m.get(str) == null ? -1 : m.get(str));
        System.out.println(i);
        m.put(str, 0);
        i = (int)m.get(str);
        System.out.println(i);
    }

}
