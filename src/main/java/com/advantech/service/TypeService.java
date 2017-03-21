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
public class TypeService {

    private final TypeDAO typeDAO;

    public TypeService() {
        typeDAO = new TypeDAO();
    }

    public Collection findAll() {
        return typeDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return typeDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Object obj) {
        return typeDAO.insert(obj);
    }

    public int update(Object obj) {
        return typeDAO.update(obj);
    }

    public int delete(Object pojo) {
        return typeDAO.delete(pojo);
    }

}
