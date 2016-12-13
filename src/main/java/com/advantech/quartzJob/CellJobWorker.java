/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.service.BasicLineTypeFacade;
import com.advantech.service.CellLineTypeFacade;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class CellJobWorker implements Job {

    private static final Logger log = LoggerFactory.getLogger(CellJobWorker.class);
    private final BasicLineTypeFacade cF = CellLineTypeFacade.getInstance();

    public CellJobWorker() {

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        //Processing Cells data & output to db or txt
        this.processingCellData();
    }
    
    private void processingCellData(){
        try{
            cF.processingDataAndSave();
        }catch(Exception ex){
            log.error(ex.toString());
        }
    }
}
