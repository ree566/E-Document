/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Auto close all cell processing PO on 10:00 pm
 */
package com.advantech.quartzJob;

import com.advantech.entity.Cell;
import com.advantech.model.BasicDAO;
import com.advantech.service.BasicService;
import com.advantech.service.CellService;
import java.util.ArrayList;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class CellDataSaver implements Job {

    private static final Logger log = LoggerFactory.getLogger(CellDataSaver.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        saveCellData();
    }

    private boolean saveCellData() {
        CellService cellService = BasicService.getCellService();
        List<Cell> processing = cellService.getCellProcessing();
        return cellService.delete(processing);
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        CellService cellService = BasicService.getCellService();
        List<Cell> l = cellService.getAll();
        List del = new ArrayList();
        for (Cell cell : l) {
            if (cell.getId() % 3 == 0) {
                del.add(cell);
            }
        }
        cellService.delete(del);
    }
}
