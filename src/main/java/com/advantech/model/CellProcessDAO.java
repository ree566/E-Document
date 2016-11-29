/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.CellProcess;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CellProcessDAO extends BasicDAO {

    public CellProcessDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<CellProcess> queryCellProcessTable(String sql, Object... params) {
        return queryForBeanList(getConn(), CellProcess.class, sql, params);
    }

    public List<CellProcess> getCellProcess() {
        return queryCellProcessTable("SELECT * FROM CellProcess");
    }

    public List<CellProcess> getCellProcess(String PO) {
        return queryCellProcessTable("SELECT * FROM CellProcess WHERE PO = ?", PO);
    }

    public boolean insertCellProcess(List<CellProcess> l) {
        return update(getConn(), "INSERT INTO CellProcess(lineId, PO) VALUES(?,?)", l, "lineId", "PO");
    }

    public boolean deleteCellProcess(String PO) {
        return update(getConn(), "DELETE FROM CellProcess WHERE PO = ?", PO);
    }

}
