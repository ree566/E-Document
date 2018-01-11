/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabSensorLoginRecordDAO;
import com.advantech.model.BabSensorLoginRecord;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class BabSensorLoginRecordService {

    @Autowired
    private BabSensorLoginRecordDAO babSensorLoginRecordDAO;

    public List<BabSensorLoginRecord> findAll() {
        return babSensorLoginRecordDAO.findAll();
    }

    public BabSensorLoginRecord findByPrimaryKey(Object obj_id) {
        return babSensorLoginRecordDAO.findByPrimaryKey(obj_id);
    }

    public BabSensorLoginRecord findBySensor(String tagName) {
        return babSensorLoginRecordDAO.findBySensor(tagName);
    }

    public int insert(BabSensorLoginRecord pojo) {
        return babSensorLoginRecordDAO.insert(pojo);
    }

    public int update(BabSensorLoginRecord pojo) {
        return babSensorLoginRecordDAO.update(pojo);
    }

    public int delete(BabSensorLoginRecord pojo) {
        return babSensorLoginRecordDAO.delete(pojo);
    }

}
