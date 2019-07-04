/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.PreAssyModuleTypeDAO;
import com.advantech.model.PreAssyModuleType;
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
public class PreAssyModuleTypeService {

    @Autowired
    private PreAssyModuleTypeDAO dao;

    public List<PreAssyModuleType> findAll() {
        return dao.findAll();
    }

    public List<PreAssyModuleType> findByModelName(String modelName) {
        return dao.findByModelName(modelName);
    }

    public PreAssyModuleType findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<PreAssyModuleType> findByPrimaryKeys(Integer... obj_ids) {
        return dao.findByPrimaryKeys(obj_ids);
    }

    public int insert(PreAssyModuleType pojo) {
        return dao.insert(pojo);
    }

    public int update(PreAssyModuleType pojo) {
        return dao.update(pojo);
    }

    public int delete(PreAssyModuleType pojo) {
        return dao.delete(pojo);
    }

}
