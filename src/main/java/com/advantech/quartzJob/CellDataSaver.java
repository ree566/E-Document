/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Auto close all cell processing PO on 10:00 pm
 */
package com.advantech.quartzJob;

import com.advantech.entity.Cell;
import com.advantech.service.CellService;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class CellDataSaver implements Job {

    private static final Logger log = LoggerFactory.getLogger(CellDataSaver.class);
    
    @Autowired
    private CellService cellService;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        saveCellData();
    }

    private boolean saveCellData() {
        List<Cell> processing = cellService.getCellProcessing();
        return cellService.delete(processing);
    }

}
