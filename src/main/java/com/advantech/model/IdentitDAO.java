/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Identit;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class IdentitDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    public List<Map> getIdentitView() {
        return queryForMapList(this.getConn(), "SELECT * FROM identitView");
    }

    public List<Map> getIdentitViewByPermission() {
        return queryForMapList(this.getConn(), "SELECT * FROM identitView WHERE permission > 0");
    }

    public List<Map> getIdentitViewByPermission(int sitefloor) {
        return queryForMapList(this.getConn(), "SELECT * FROM identitView WHERE permission > 0 AND sitefloor = ?", sitefloor);
    }

    public Identit getIdentit(String jobnumber) {
        List l = queryIdentitTable("SELECT * FROM [M3-SERVER\\M3SERVER,1433].[WebAccess].[dbo].[identitView] WHERE jobnumber = ?", jobnumber);
        return !l.isEmpty() ? (Identit) l.get(0) : null;
    }

    private List<Identit> queryIdentitTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Identit.class, sql, params);
    }

}
