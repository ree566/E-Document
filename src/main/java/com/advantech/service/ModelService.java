/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class ModelService implements BasicService{

    private final ModelDAO modelDAO;

    public ModelService() {
        modelDAO = new ModelDAO();
    }

    @Override
    public Collection findAll() {
        return modelDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return modelDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return modelDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return modelDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return modelDAO.delete(pojo);
    }

   
}
