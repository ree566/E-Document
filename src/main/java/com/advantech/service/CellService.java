/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Cell;
import com.advantech.model.CellDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CellService {

    @Autowired
    private CellDAO cellDAO;

    public List<Cell> getAll() {
        return cellDAO.getAll();
    }

    public Cell getOne(int id) {
        return cellDAO.getOne(id);
    }

    public List<Cell> getByPo(String PO) {
        return cellDAO.getByPo(PO);
    }

    public List<Cell> getCellProcessing() {
        return cellDAO.getCellProcessing();
    }

    public List<Cell> getCellProcessing(int lineId) {
        return cellDAO.getCellProcessing(lineId);
    }

    public List<Map> cellHistoryView() {
        return cellDAO.cellHistoryView();
    }

    public List<Map> cellHistoryView(String startDate, String endDate) {
        return cellDAO.cellHistoryView(startDate, endDate);
    }

    public boolean insert(Cell cell) {
        List l = new ArrayList();
        l.add(cell);
        return this.insert(l);
    }

    public boolean insert(List<Cell> l) {
        return cellDAO.insert(l);
    }

    public boolean delete(List<Cell> l) {
        return cellDAO.delete(l);
    }

    public boolean delete(Cell cell) {
        return cellDAO.delete(cell);
    }

    public boolean insertAlarm(List<AlarmAction> l) {
        return cellDAO.insertAlarm(l);
    }

    public boolean updateAlarm(List<AlarmAction> l) {
        return cellDAO.updateAlarm(l);
    }

    public boolean resetAlarm() {
        return cellDAO.resetAlarm();
    }

    public boolean removeAlarmSign() {
        return cellDAO.removeAlarmSign();
    }

}
