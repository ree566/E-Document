/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.ModelSopRemarkEventDAO;
import com.advantech.model.ModelSopRemarkEvent;
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
public class ModelSopRemarkEventService {

    @Autowired
    private ModelSopRemarkEventDAO modelSopRemarkEventDAO;

    public List<ModelSopRemarkEvent> findAll() {
        return modelSopRemarkEventDAO.findAll();
    }

    public ModelSopRemarkEvent findByPrimaryKey(Object obj_id) {
        return modelSopRemarkEventDAO.findByPrimaryKey(obj_id);
    }

    public int insert(ModelSopRemarkEvent pojo) {
        return modelSopRemarkEventDAO.insert(pojo);
    }

    public int update(ModelSopRemarkEvent pojo) {
        return modelSopRemarkEventDAO.update(pojo);
    }

    public int delete(ModelSopRemarkEvent pojo) {
        return modelSopRemarkEventDAO.delete(pojo);
    }

}
