package com.advantech.service;

import com.advantech.dao.CountermeasureSopRecordDAO;
import com.advantech.model.CountermeasureSopRecord;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CountermeasureSopRecordService {

    @Autowired
    private CountermeasureSopRecordDAO countermeasureSopRecordDAO;

    public List<CountermeasureSopRecord> findAll() {
        return countermeasureSopRecordDAO.findAll();
    }

    public CountermeasureSopRecord findByPrimaryKey(Object obj_id) {
        return countermeasureSopRecordDAO.findByPrimaryKey(obj_id);
    }

    public CountermeasureSopRecord findByCountermeasure(int cm_id) {
        return countermeasureSopRecordDAO.findByCountermeasure(cm_id);
    }

    public int insert(CountermeasureSopRecord pojo) {
        return countermeasureSopRecordDAO.insert(pojo);
    }

    public int update(CountermeasureSopRecord pojo) {
        return countermeasureSopRecordDAO.update(pojo);
    }

    public int delete(CountermeasureSopRecord pojo) {
        return countermeasureSopRecordDAO.delete(pojo);
    }

}
