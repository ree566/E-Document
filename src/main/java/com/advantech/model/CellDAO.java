/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Cell;
import com.advantech.interfaces.AlarmActions;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class CellDAO extends BasicDAO implements AlarmActions {

    public CellDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<Cell> query(String sql, Object... params) {
        return queryForBeanList(getConn(), Cell.class, sql, params);
    }

    public List<Cell> getAll() {
        return query("SELECT * FROM Cell");
    }

    public Cell getOne(int id) {
        List l = query("SELECT * FROM Cell WHERE id = ?", id);
        return l.isEmpty() ? null : (Cell) l.get(0);
    }

    public List<Cell> getByPo(String PO) {
        return query("SELECT * FROM Cell WHERE PO = ?", PO);
    }

    public List<Cell> getCellProcessing() {
        return query("SELECT * FROM cellProcessing");
    }

    public List<Cell> getCellProcessing(int lineId) {
        return query("SELECT * FROM cellProcessing WHERE lineId = ?", lineId);
    }

    public List<Map> cellHistoryView() {
        return queryForMapList(this.getConn(), "SELECT * FROM cellHistoryView");
    }

    public List<Map> cellHistoryView(String startDate, String endDate) {
        return queryForMapList(this.getConn(), "SELECT * FROM cellHistoryView WHERE CONVERT(VARCHAR(10), btime, 20) BETWEEN ? AND ?", startDate, endDate);
    }

    //"Model_name" 在dbutil get property時會去取 "model_name"
    public boolean insert(List<Cell> l) {
        return update(getConn(), "INSERT INTO Cell(lineId, PO, Model_name) VALUES(?,?,?)", l, "lineId", "PO", "model_name");
    }

    public boolean delete(List<Cell> l) {
        return update(getConn(), "UPDATE Cell SET isused = 2 WHERE id = ?", l, "id");
    }

    public boolean delete(Cell cell) {
        return update(getConn(), "UPDATE Cell SET isused = 1 WHERE id = ?", cell.getId());
    }

    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return updateAlarmTable("INSERT INTO Alm_CellAction(alarm, tableId) VALUES(?, ?)", l);
    }

    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return updateAlarmTable("UPDATE Alm_CellAction SET alarm = ? WHERE tableId = ?", l);
    }

    @Override
    public boolean resetAlarm() {
        return update(getConn(), "UPDATE Alm_CellAction SET alarm = 0");
    }

    @Override
    public boolean removeAlarmSign() {
        return update(getConn(), "TRUNCATE TABLE Alm_CellAction");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }
}
