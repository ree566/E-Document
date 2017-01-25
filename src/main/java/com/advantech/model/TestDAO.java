/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Desk;
import com.advantech.entity.Test;
import com.advantech.entity.TestLineTypeUser;
import com.advantech.interfaces.AlarmActions;
import com.advantech.service.TestLineTypeFacade;
import com.advantech.test.TestClass2;
import com.google.gson.Gson;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestDAO extends BasicDAO implements AlarmActions {

    public TestDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    public List<Test> getTableInfo() {
        return getAllTableInfo();
    }

    public Test getTableInfo(int tableNo) {
        List<Test> l = queryTestTable("SELECT * FROM testTableView WHERE ID = ?", tableNo);
        return !l.isEmpty() ? l.get(0) : null;
    }

    public Test getTableInfo(int tableNo, String jobnumber) {
        List<Test> l = queryTestTable("SELECT * FROM testTableView WHERE ID = ? AND userid = ?", tableNo, jobnumber);
        return !l.isEmpty() ? l.get(0) : null;
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

    public List<Desk> getDesk(String sitefloor) {
        if (sitefloor.length() > 3) {
            return new ArrayList();
        }
        return queryDeskTable("SELECT * FROM LS_Table where sitefloor = ?", sitefloor);
    }

    public List<Map> getRecordTestLineType(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL testLineRecord(?,?)}", startDate, endDate);
    }

    public boolean insertTestPeople(int tableNo, String jobnumber) {
        return updateTestTable("INSERT INTO LS_TEST(id,userid) VALUES (?,?)", tableNo, jobnumber);
    }

    public boolean recordTestLineType(List<TestLineTypeUser> l) {
        return update(
                getConn(),
                "INSERT INTO testLineTypeRecord(user_id, user_name, productivity) VALUES(?,?,?)",
                l,
                "userNo", "userName", "productivity"
        );
    }

    public boolean changeDeck(int tableNo, String jobnumber) {
        return updateTestTable("UPDATE LS_TEST SET id = ? WHERE userid = ?", tableNo, jobnumber);
    }

    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return updateAlarmTable("INSERT INTO Alm_TestAction(alarm, tableId) VALUES(?, ?)", l);
    }

    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return updateAlarmTable("UPDATE Alm_TestAction SET alarm = ? WHERE tableId = ?", l);
    }

    @Override
    public boolean resetAlarm() {
        return update(getConn(), "UPDATE Alm_TestAction SET alarm = 0");
    }

    @Override
    public boolean removeAlarmSign() {
        return update(getConn(), "TRUNCATE TABLE Alm_TestAction");
    }

    public boolean setTestAlarmToTestingMode() {
        return update(getConn(), "UPDATE Alm_TestAction SET alarm = 1");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }

    private boolean updateTestTable(String sql, Object... params) {
        return update(getConn(), sql, params);
    }

    //JSTL read data
    public Map getPeopleNotInDB() {
//        return DataTransformer.getPeopleNotMatch();
        return TestLineTypeFacade.getInstance().getPEOPLE_NOT_MATCH();
    }

    public boolean deleteTestPeople(int tableNo) {
        return updateTestTable("DELETE LS_TEST WHERE id=?", tableNo);
    }

    public boolean cleanTestTable() {
        return updateTestTable("TRUNCATE TABLE LS_TEST");
    }

    public static void main(String str[]) {
        BasicDAO.dataSourceInit1();
        TestDAO dao = new TestDAO();
        List<TestClass2> l = BasicDAO.queryForBeanList(dao.getConn(), TestClass2.class, "SELECT * FROM LineTypeConfig");
        System.out.println(new Gson().toJson(l));
    }
}

