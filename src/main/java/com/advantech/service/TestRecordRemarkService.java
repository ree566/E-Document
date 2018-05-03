/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.TestRecordDAO;
import com.advantech.dao.TestRecordRemarkDAO;
import com.advantech.model.ReplyStatus;
import com.advantech.model.TestRecord;
import com.advantech.model.TestRecordRemark;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TestRecordRemarkService {

    @Autowired
    private TestRecordRemarkDAO testRecordRemarkDAO;

    @Autowired
    private TestRecordDAO testRecordDAO;

    public List<TestRecordRemark> findAll() {
        return testRecordRemarkDAO.findAll();
    }

    public TestRecordRemark findByPrimaryKey(Object obj_id) {
        return testRecordRemarkDAO.findByPrimaryKey(obj_id);
    }

    public TestRecordRemark findByTestRecord(int recordId) {
        return testRecordRemarkDAO.findByTestRecord(recordId);
    }

    public int insert(TestRecordRemark pojo) {
        testRecordRemarkDAO.insert(pojo);
        TestRecord record = pojo.getTestRecord();
        record.setReplyStatus(ReplyStatus.REPLIED);
        testRecordDAO.update(record);
        return 1;
    }

    public int update(TestRecordRemark pojo) {
        return testRecordRemarkDAO.update(pojo);
    }

    public int delete(TestRecordRemark pojo) {
        return testRecordRemarkDAO.delete(pojo);
    }

}
