/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Cell;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class CellDAO extends BasicDAO {

    public CellDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<Cell> queryCellProcessTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Cell.class, sql, params);
    }

    public List<Cell> getCellProcess() {
        return queryCellProcessTable("SELECT * FROM Cell");
    }

    public List<Cell> getCellProcess(String PO) {
        return queryCellProcessTable("SELECT * FROM Cell WHERE PO = ?", PO);
    }

    public List<Map> getCellPerPcsHistory(String PO) {
        return queryProcForMapList(this.getConn(), "{CALL cellDiffPerPcs(?)}", PO);
    }

    public boolean insertCellProcess(List<Cell> l) {
        return update(getConn(), "INSERT INTO Cell(lineId, PO) VALUES(?,?)", l, "lineId", "PO");
    }

    public boolean deleteCellProcess(String PO) {
        return update(getConn(), "DELETE FROM Cell WHERE PO = ?", PO);
    }

}
