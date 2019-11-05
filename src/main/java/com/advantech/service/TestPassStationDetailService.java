/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.TestPassStationDetailDAO;
import com.advantech.model.TestPassStationDetail;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TestPassStationDetailService {

    @Autowired
    private TestPassStationDetailDAO dao;

    public List<TestPassStationDetail> findAll() {
        return dao.findAll();
    }

    public TestPassStationDetail findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<TestPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return dao.findByDate(sD, eD);
    }

    public int insert(TestPassStationDetail pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<TestPassStationDetail> l) {
        dao.insert(l);
        return 1;
    }

    public int update(TestPassStationDetail pojo) {
        return dao.update(pojo);
    }

    public int delete(TestPassStationDetail pojo) {
        return dao.delete(pojo);
    }

    public int delete(List<TestPassStationDetail> l) {
        return dao.delete(l);
    }

}
