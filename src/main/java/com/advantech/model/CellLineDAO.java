/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.CellLine;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CellLineDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    public List<CellLine> findAll() {
        return queryForBeanList(this.getConn(), CellLine.class, "SELECT * FROM CellLine");
    }

    public CellLine findOne(int id) {
        List l = queryForBeanList(this.getConn(), CellLine.class, "SELECT * FROM CellLine WHERE id = ?", id);
        return l.isEmpty() ? null : (CellLine) l.get(0);
    }

    public List<CellLine> findBySitefloor(int sitefloor) {
        return queryForBeanList(this.getConn(), CellLine.class, "SELECT * FROM CellLine WHERE sitefloor = ?", sitefloor);
    }

    public boolean updateStatus(int status) {
        return update(this.getConn(), "UPDATE CellLine set isused = ?", status);
    }

    public boolean updateStatus(int id, int status) {
        return update(this.getConn(), "UPDATE CellLine set isused = ? WHERE id = ?", status, id);
    }

}
