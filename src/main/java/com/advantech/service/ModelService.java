/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Model;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class ModelService implements BasicService {

    private final ModelDAO modelDAO;

    public ModelService() {
        modelDAO = new ModelDAO();
    }

    public Collection findAll() {
        return modelDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return modelDAO.findByPrimaryKey(obj_id);
    }

    public List<Model> findByPrimaryKeys(Integer... id) {
        return modelDAO.findByPrimaryKeys(id);
    }

    public Model findByName(String modelName) {
        return modelDAO.findByName(modelName);
    }

    public int insert(Object obj) {
        return modelDAO.insert(obj);
    }

    public int update(Object obj) {
        return modelDAO.update(obj);
    }

    public int delete(Object pojo) {
        return modelDAO.delete(pojo);
    }

}
