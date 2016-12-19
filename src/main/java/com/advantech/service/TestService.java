/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Test;
import com.advantech.entity.TestLineTypeUser;
import com.advantech.interfaces.AlarmActions;
import com.advantech.model.TestDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestService implements AlarmActions{

    private final TestDAO testDAO;

    protected TestService() {
        testDAO = new TestDAO();
    }

    public List<Test> getAllTableInfo() {
        return testDAO.getAllTableInfo();
    }

    public Test getTableInfo(int tableNo) {
        return testDAO.getTableInfo(tableNo);
    }

    public Test getTableInfo(int tableNo, String jobNumber) {
        return testDAO.getTableInfo(tableNo, jobNumber);
    }

    public List<Map> getRecordTestLineType(String startDate, String endDate) {
        return testDAO.getRecordTestLineType(startDate, endDate);
    }

    public boolean addTestPeople(int tableNo, String jobNumber) {
        return testDAO.insertTestPeople(tableNo, jobNumber);
    }

    public boolean recordTestLineType(List<TestLineTypeUser> l) {
        return testDAO.recordTestLineType(l);
    }

    public boolean changeDeck(int tableNo, String jobnumber) {
        return testDAO.changeDeck(tableNo, jobnumber);
    }
    
    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return testDAO.insertAlarm(l);
    }

    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return testDAO.updateAlarm(l);
    }

    @Override
    public boolean resetAlarm() {
        return testDAO.resetAlarm();
    }
    
    @Override
    public boolean removeAlarmSign() {
        return testDAO.removeAlarmSign();
    }
    
    public boolean setTestAlarmToTestingMode(){
        return testDAO.setTestAlarmToTestingMode();
    }

    public String checkDeskIsAvailable(int tableNo) {
        Test t = this.getTableInfo(tableNo);
        if (t != null) {
            return "此桌次已有使用者";
        } else {
            return null;
        }
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

    public boolean removeTestPeople(int tableNo, String jobNumber) {
        return testDAO.deleteTestPeople(tableNo);
    }

    public boolean cleanTestTable() {
        return testDAO.cleanTestTable();
    }
}
