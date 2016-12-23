/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Cell;
import com.advantech.interfaces.AlarmActions;
import com.advantech.model.CellDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class CellService implements AlarmActions {

    private final CellDAO cellProcessDAO;

    protected CellService() {
        cellProcessDAO = new CellDAO();
    }

    public List<Cell> getCell() {
        return cellProcessDAO.getCell();
    }

    public List<Cell> getCell(String PO) {
        return cellProcessDAO.getCell(PO);
    }

    public List<Cell> getCellProcessing() {
        return cellProcessDAO.getCellProcessing();
    }

    public List<Cell> getCellProcessing(int lineId) {
        return cellProcessDAO.getCellProcessing(lineId);
    }

    public List<Map> cellHistoryView() {
        return cellProcessDAO.cellHistoryView();
    }
    
    public List<Map> cellHistoryView(String startDate, String endDate) {
        return cellProcessDAO.cellHistoryView(startDate, endDate);
    }

    public boolean insertCell(Cell cell) {
        List l = new ArrayList();
        l.add(cell);
        return this.insertCell(l);
    }

    public boolean insertCell(List<Cell> l) {
        return cellProcessDAO.insertCell(l);
    }
    
    public boolean deleteCell(List<Cell> l) {
        return cellProcessDAO.deleteCell(l);
    }

    public boolean deleteCell(Cell cell) {
        return cellProcessDAO.deleteCell(cell);
    }

    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return cellProcessDAO.insertAlarm(l);
    }

    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return cellProcessDAO.updateAlarm(l);
    }

    @Override
    public boolean resetAlarm() {
        return cellProcessDAO.resetAlarm();
    }

    @Override
    public boolean removeAlarmSign() {
        return cellProcessDAO.removeAlarmSign();
    }
}
