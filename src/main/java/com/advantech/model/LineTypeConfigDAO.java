/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.LineTypeConfig;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class LineTypeConfigDAO extends BasicDAO {

    public LineTypeConfigDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<LineTypeConfig> query(String sql, Object... params) {
        return queryForBeanList(getConn(), LineTypeConfig.class, sql, params);
    }

    public List<LineTypeConfig> getAll() {
        return query("SELECT * FROM LineTypeConfig");
    }

    public LineTypeConfig getOne(int id) {
        List l = query("SELECT * FROM LineTypeConfig WHERE id = ?", id);
        return !l.isEmpty() ? (LineTypeConfig) l.get(0) : null;
    }

    public boolean insert(List<LineTypeConfig> l) {
        return update(getConn(), "INSERT INTO LineTypeConfig(linetype_name, variable_name, variable_value) VALUES(?,?,?)", l, "linetype_name", "variable_name", "variable_value");
    }

    public boolean update(List<LineTypeConfig> l) {
        return update(getConn(), "UPDATE LineTypeConfig SET linetype_name = ?, variable_name = ?, variable_value = ? WHERE id = ?", l, "linetype_name", "variable_name", "variable_value", "id");
    }

    public boolean updateValue(List<LineTypeConfig> l) {
        return update(getConn(), "UPDATE LineTypeConfig SET variable_value = ? WHERE linetype_name = ? AND variable_name = ?", l, "variable_value", "linetype_name", "variable_name");
    }

    public boolean delete(LineTypeConfig lineTypeConfig) {
        return update(getConn(), "DELETE FROM LineTypeConfig WHERE id = ?", lineTypeConfig.getId());
    }

}
