/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.MesPassCountRecordDAO;
import com.advantech.model.MesPassCountRecord;
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
public class MesPassCountService {

    @Autowired
    private MesPassCountRecordDAO dao;

    public List<MesPassCountRecord> findAll() {
        return dao.findAll();
    }

    public MesPassCountRecord findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(MesPassCountRecord pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<MesPassCountRecord> l) {
        return dao.insert(l);
    }

    public int update(MesPassCountRecord pojo) {
        return dao.update(pojo);
    }

    public int delete(MesPassCountRecord pojo) {
        return dao.delete(pojo);
    }

}
