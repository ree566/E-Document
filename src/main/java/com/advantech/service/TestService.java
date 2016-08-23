/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.model.TestDAO;
import com.advantech.entity.Test;
import com.advantech.entity.TestLineTypeUser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestService {

    private final TestDAO testDAO;

    protected TestService() {
        testDAO = new TestDAO();
    }

    public List<Test> getAllTableInfo() {
        return testDAO.getAllTableInfo();
    }

    public List<Map> getRecordTestLineType(String startDate, String endDate) {
        return testDAO.getRecordTestLineType(startDate, endDate);
    }

    public boolean addTestPeople(int tableNum, String jobNumber) {
        return testDAO.insertTestPeople(tableNum, jobNumber);
    }

    public boolean recordTestLineType(List<TestLineTypeUser> l) {
        return testDAO.recordTestLineType(l);
    }

    public boolean removeTestPeople(int tableNum, String jobNumber) {
        return testDAO.deleteTestPeople(tableNum, jobNumber);
    }

    public boolean updateTestAlarm(List<AlarmAction> l) {
        return testDAO.updateTestAlarm(l);
    }

    public boolean resetTestAlarm() {
        return testDAO.resetTestAlarm();
    }

    public String checkDeskIsAvailable(int tableNo, String userNo) {
        List<Test> tables = testDAO.getAllTableInfo();
        if (tables != null && !tables.isEmpty()) {
            for (Test table : tables) {
                if (table.getId() == tableNo) {
                    return "此桌次已有使用者";
                }
                if (table.getUserid().equals(userNo)) {
                    return "您的工號已經在桌次" + table.getId() + "使用中";
                }
            }
        }
        return null;
    }

    public boolean cleanTestTable() {
        return testDAO.cleanTestTable();
    }
}
