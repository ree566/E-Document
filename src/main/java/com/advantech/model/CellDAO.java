/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Cell;
import com.advantech.interfaces.AlarmActions;
import static com.advantech.model.BasicDAO.update;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CellDAO extends BasicDAO implements AlarmActions{

    public CellDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<Cell> queryCellTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Cell.class, sql, params);
    }

    public List<Cell> getCell() {
        return queryCellTable("SELECT * FROM Cell");
    }

    public List<Cell> getCell(String PO) {
        return queryCellTable("SELECT * FROM Cell WHERE PO = ?", PO);
    }
    
    public List<Cell> getCellProcessing(){
        return queryCellTable("SELECT * FROM cellProcessing");
    }

    public boolean insertCell(List<Cell> l) {
        return update(getConn(), "INSERT INTO Cell(lineId, PO) VALUES(?,?)", l, "lineId", "PO");
    }

    public boolean deleteCell(int lineId, String PO) {
        return update(getConn(), "UPDATE Cell SET isused = 1 WHERE lineId = ? AND PO = ?", lineId, PO);
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
