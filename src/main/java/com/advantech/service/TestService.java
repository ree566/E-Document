/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Test;
import com.advantech.dao.TestDAO;
import com.advantech.model.AlarmTestAction;
import com.advantech.model.TestTable;
import com.advantech.webservice.WebServiceTX;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestService {

    @Autowired
    private TestDAO testDAO;

    @Autowired
    private TestTableService testTableService;

    public List<Test> findAll() {
        return testDAO.findAll();
    }

    public Test findByPrimaryKey(Object obj_id) {
        return testDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Test pojo) {
        return testDAO.insert(pojo);
    }

    public int insert(int table_id, String jobnumber) {
        TestTable table = testTableService.findByPrimaryKey(table_id);
        checkDeskIsAvailable(table);
        checkUserIsAvailable(jobnumber);
        Test t = new Test(table, jobnumber);
        this.insert(t);
        WebServiceTX.getInstance().kanbanUserLogin(jobnumber);
        return 1;
    }

    public void checkDeskIsAvailable(TestTable t) {
        if (t.getTests() != null && !t.getTests().isEmpty()) {
            throw new IllegalArgumentException("此桌次已有使用者");
        }
    }

    public void checkUserIsAvailable(String jobNumber) {
        Test t = testDAO.findByJobnumber(jobNumber);
        if (t != null) {
            throw new IllegalArgumentException("使用者已在桌次 " + t.getTestTable().getName() + " 使用中");
        }
    }

    public int update(Test pojo) {
        return testDAO.update(pojo);
    }

    public int changeDeck(String jobnumber) {
        Test t = testDAO.findByJobnumber(jobnumber);
        this.delete(t);
        return 1;
    }

    public int delete(Test pojo) {
        return testDAO.delete(pojo);
    }

    public int delete(String jobnumber) {
        Test t = testDAO.findByJobnumber(jobnumber);
        this.delete(t);
        WebServiceTX.getInstance().kanbanUserLogout(jobnumber);
        return 1;
    }

    public int cleanTests() {
        return testDAO.cleanTests();
    }

    public boolean insertAlarm(List<AlarmTestAction> l) {
        return testDAO.insertAlarm(l);
    }

    public boolean updateAlarm(List<AlarmTestAction> l) {
        return testDAO.updateAlarm(l);
    }

    public boolean resetAlarm() {
        return testDAO.resetAlarm();
    }

    public boolean removeAlarmSign() {
        return testDAO.removeAlarmSign();
    }

    public boolean setTestAlarmToTestingMode() {
        return testDAO.setTestAlarmToTestingMode();
    }

}
