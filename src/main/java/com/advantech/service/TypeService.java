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
public class TypeService implements BasicService{

    private final TypeDAO typeDAO;

    public TypeService() {
        typeDAO = new TypeDAO();
    }

    @Override
    public Collection findAll() {
        return typeDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return typeDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return typeDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return typeDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return typeDAO.delete(pojo);
    }

   
}
