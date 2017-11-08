/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Bab;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng 前置的bab
 */
@Repository
public class BabPreDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BabPreDAO.class);

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<Bab> query(String sql, Object... params) {
        return queryForBeanList(getConn(), Bab.class, sql, params);
    }

    public List<Bab> findAll() {
        return query("SELECT * FROM LS_BAB_pre");
    }

    public Bab findByPrimaryKey(int id) {
        List l = query("SELECT * FROM LS_BAB_pre WHERE id = ?", id);
        return !l.isEmpty() ? (Bab) l.get(0) : null;
    }

    public boolean insert(Bab bab) {
        return update(
                getConn(),
                "INSERT INTO LS_BAB_pre(PO,Model_name,line,people,startPosition) VALUES (?,?,?,?,?)",
                bab.getPO(),
                bab.getModel_name(),
                bab.getLine(),
                bab.getPeople(),
                bab.getStartPosition()
        );
    }

    public boolean update(Bab bab) {
        return update(
                getConn(),
                "UPDATE LS_BAB_pre SET isused = ? WHERE id = ?",
                bab.getIsused(),
                bab.getId()
        );
    }

    public boolean delete(int id) {
        throw new UnsupportedOperationException();
    }

}
