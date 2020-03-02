/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.BabCollectModeChangeEventDAO;
import com.advantech.model.db1.BabCollectModeChangeEvent;
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
public class BabCollectModeChangeEventService {

    @Autowired
    private BabCollectModeChangeEventDAO dao;

    public List<BabCollectModeChangeEvent> findAll() {
        return dao.findAll();
    }

    public BabCollectModeChangeEvent findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(BabCollectModeChangeEvent pojo) {
        return dao.insert(pojo);
    }

    public int update(BabCollectModeChangeEvent pojo) {
        return dao.update(pojo);
    }

    public int delete(BabCollectModeChangeEvent pojo) {
        return dao.delete(pojo);
    }

}
