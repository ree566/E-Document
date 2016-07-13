/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.quartzJob.DataTransformer;
import com.advantech.entity.Test;
import com.advantech.entity.Desk;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestDAO extends BasicDAO {

    public TestDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    public List<Test> getTableInfo() {
        return getAllTableInfo();
    }

    private List queryTestTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Test.class, sql, params);
    }

    private List queryDeskTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Desk.class, sql, params);
    }

    public List<Test> getAllTableInfo() {
        return queryTestTable("SELECT * FROM testTableView ORDER BY ID");
    }

    public List<Desk> getDesk() {
        return queryDeskTable("SELECT * FROM LS_Table");
    }

    public boolean insertTestPeople(int tableNo, String userNo) {
        return updateTestTable("INSERT INTO LS_TEST(id,userid) VALUES (?,?)", tableNo, userNo);
    }

    public boolean deleteTestPeople(int tableNo, String userNo) {
        return updateTestTable("DELETE LS_TEST WHERE id=?", tableNo);
    }

    private boolean updateTestTable(String sql, Object... params) {
        return update(getConn(), sql, params);
    }

    //JSTL read data
    public Map getPeopleNotInDB() {
        return DataTransformer.getPeopleNotMatch();
    }

    public boolean cleanTestTable() {
        return updateTestTable("TRUNCATE TABLE LS_TEST");
    }

    public static void main(String str[]) {
    }
}
