/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.CELL;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CELLDAO extends BasicDAO {

    private final int LINE_OPEN_SIGN = 1;
    private final int LINE_CLOSE_SIGN = 0;

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    public List<CELL> findAll() {
        return queryForBeanList(this.getConn(), CELL.class, "SELECT * FROM LS_CELL");
    }

    public CELL findOne(int id) {
        List l = queryForBeanList(this.getConn(), CELL.class, "SELECT * FROM LS_CELL WHERE id = ?", id);
        return l.isEmpty() ? null : (CELL) l.get(0);
    }

}
