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
public class ActionCodeDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    public List<Map> findAll() {
        return queryForMapList(getConn(), "SELECT * FROM actionCode");
    }

    public boolean insert(int cm_id, List<String> actionCodes) {
        Connection conn = this.getConn();
        String query = "INSERT INTO CountermeasureDetail(cm_id, ac_id) values(?,?)";
        for (String actionCode : actionCodes) {
            update(conn, query, cm_id, actionCode);
        }
        return true;
    }

    public boolean delete(int cm_id) {
        return update(getConn(), "DELETE FROM CountermeasureDetail WHERE cm_id = ?", cm_id);
    }
}
