/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Cell;
import com.advantech.interfaces.AlarmActions;
import com.advantech.model.BasicDAO;
import com.advantech.model.CellDAO;
import com.google.gson.Gson;
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

    public List<Cell> getAll() {
        return cellProcessDAO.getAll();
    }

    public Cell getOne(int id) {
        return cellProcessDAO.getOne(id);
    }

    public List<Cell> getByPo(String PO) {
        return cellProcessDAO.getByPo(PO);
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

    public boolean insert(Cell cell) {
        List l = new ArrayList();
        l.add(cell);
        return this.insert(l);
    }

    public boolean insert(List<Cell> l) {
        return cellProcessDAO.insert(l);
    }

    public boolean delete(List<Cell> l) {
        return cellProcessDAO.delete(l);
    }

    public boolean delete(Cell cell) {
        return cellProcessDAO.delete(cell);
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

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        CellService service = BasicService.getCellService();
//        System.out.println(service.insert(new Cell(4, "test", "test")));
//        System.out.println(new Gson().toJson());
        System.out.println(service.insert(service.getOne(10)));
    }
}
