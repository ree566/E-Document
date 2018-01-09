/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.TestRecordDAO;
import com.advantech.model.TestRecord;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TestRecordService {

    @Autowired
    private TestRecordDAO testRecordDAO;

    public List<TestRecord> findAll() {
        return testRecordDAO.findAll();
    }

    public TestRecord findByPrimaryKey(Object obj_id) {
        return testRecordDAO.findByPrimaryKey(obj_id);
    }

    public List<TestRecord> findByDate(DateTime sD, DateTime eD) {
        return testRecordDAO.findByDate(sD, eD);
    }

    public int insert(TestRecord pojo) {
        return testRecordDAO.insert(pojo);
    }

    public int insert(List<TestRecord> l) {
        for (TestRecord t : l) {
            this.insert(t);
        }
        return 1;
    }

    public int update(TestRecord pojo) {
        return testRecordDAO.update(pojo);
    }

    public int delete(TestRecord pojo) {
        return testRecordDAO.delete(pojo);
    }

}
