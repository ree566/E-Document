/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.TestDAO;
import com.advantech.entity.Test;
import java.util.List;

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
    
    public boolean addTestPeople(int tableNum, String jobNumber) {
        return testDAO.insertTestPeople(tableNum, jobNumber);
    }

    public boolean removeTestPeople(int tableNum, String jobNumber) {
        return testDAO.deleteTestPeople(tableNum, jobNumber);
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
