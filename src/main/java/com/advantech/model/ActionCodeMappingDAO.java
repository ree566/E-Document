/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.ActionCodeMapping;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ActionCodeMappingDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<ActionCodeMapping> queryActionCodeMappingTable(String sql, Object... params) {
        return queryForBeanList(getConn(), ActionCodeMapping.class, sql, params);
    }

    public List<ActionCodeMapping> getActionCodeMapping() {
        return queryActionCodeMappingTable("SELECT * FROM ActionCodeMapping");
    }

    public List<Map> getActionCodeMapping1() {
        return queryForMapList(this.getConn(), "SELECT * FROM ActionCodeMapping");
    }

    public List<ActionCodeMapping> getActionCodeMapping(int id) {
        return queryActionCodeMappingTable("SELECT * FROM ActionCodeMapping WHERE id = ?", id);
    }

    public List<ActionCodeMapping> getActionCodeMappingByActionCode(int ac_id) {
        return queryActionCodeMappingTable("SELECT * FROM ActionCodeMapping WHERE ac_id = ?", ac_id);
    }

}
