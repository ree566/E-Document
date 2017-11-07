/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Auto close all cell processing PO on 10:00 pm
 */
package com.advantech.quartzJob;

import com.advantech.entity.Cell;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.service.CellService;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class CellDataSaver extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(CellDataSaver.class);
    
    private final CellService cellService;

    public CellDataSaver() {
        cellService = (CellService) ApplicationContextHelper.getBean("cellService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        saveCellData();
    }

    private boolean saveCellData() {
        List<Cell> processing = cellService.getCellProcessing();
        return cellService.delete(processing);
    }

}
