/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.BAB;
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
public class BABPreDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BABPreDAO.class);

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<BAB> query(String sql, Object... params) {
        return queryForBeanList(getConn(), BAB.class, sql, params);
    }

    public List<BAB> findAll() {
        return query("SELECT * FROM LS_BAB_pre");
    }

    public BAB findByPrimaryKey(int id) {
        List l = query("SELECT * FROM LS_BAB_pre WHERE id = ?", id);
        return !l.isEmpty() ? (BAB) l.get(0) : null;
    }

    public boolean insert(BAB bab) {
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

    public boolean update(BAB bab) {
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
