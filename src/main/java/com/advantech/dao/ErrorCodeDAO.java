/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ErrorCodeDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    public List<Map> findAll() {
        return queryForMapList(getConn(), "SELECT * FROM errorCode");
    }

    public List<Map> findByCountermeasure(int cm_id) {
        return queryForMapList(getConn(), "SELECT * FROM CountermeasureErrorCodeView WHERE cm_id = ?", cm_id);
    }
}
