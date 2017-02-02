/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Identit;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    public Identit getIdentit(String jobnumber) {
        List l = queryIdentitTable("SELECT * FROM LeaveApplicationRecord.dbo.identit WHERE jobnumber = ?", jobnumber);
        return !l.isEmpty() ? (Identit) l.get(0) : null;
    }

    public boolean userLogin(String jobnumber, String password) {
        Identit i = this.getIdentit(jobnumber);
        return i.getPassword().equals(password);
    }

    private List<Identit> queryIdentitTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Identit.class, sql, params);
    }

    public boolean newIdentit(List beanList) {
        return alterIdentitForBean(
                "INSERT INTO identit(jobnumber, password, name, lineType, department, permission, sitefloor, email, serving) VALUES(?,?,?,?,?,?,?,?,?)",
                beanList,
                "jobnumber", "password", "name", "lineType", "department", "permission", "sitefloor", "email", "serving");
    }

    public boolean updateIdentit(List beanList) {
        return alterIdentitForBean(
                "UPDATE identit SET password = ?, name = ?, lineType = ?, department = ?, permission = ?, sitefloor = ? , email=? WHERE id = ?",
                beanList,
                "password", "name", "lineType", "department", "permission", "sitefloor", "email", "id");
    }

    public boolean updateUsersPassword(int userNo, String password) {
        return alterIdentit("UPDATE identit SET password = ? WHERE id = ?", password, userNo);
    }

    public boolean updateIdentitServingStatus(int status, int userNo) {
        return alterIdentit("UPDATE identit SET serving = ? WHERE id = ?", status, userNo);
    }

    private boolean alterIdentit(String sql, Object... params) {
        return update(getConn(), sql, params);
    }

    private boolean alterIdentitForBean(String sql, List beanList, String... propertyNames) {
        return update(getConn(), sql, beanList, propertyNames);
    }
}
