/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.ModelSopRemarkDAO;
import com.advantech.dao.ModelSopRemarkEventDAO;
import com.advantech.model.ModelSopRemark;
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
public class ModelSopRemarkService {

    @Autowired
    private ModelSopRemarkDAO modelSopRemarkDAO;
    
    @Autowired
    private ModelSopRemarkEventDAO modelSopRemarkEventDAO;

    public List<ModelSopRemark> findAll() {
        return modelSopRemarkDAO.findAll();
    }

    public ModelSopRemark findByPrimaryKey(Object obj_id) {
        return modelSopRemarkDAO.findByPrimaryKey(obj_id);
    }

    public int insert(ModelSopRemark pojo) {
        return modelSopRemarkDAO.insert(pojo);
    }

    public int update(ModelSopRemark pojo) {
        return modelSopRemarkDAO.update(pojo);
    }

    public int delete(ModelSopRemark pojo) {
        return modelSopRemarkDAO.delete(pojo);
    }

}
