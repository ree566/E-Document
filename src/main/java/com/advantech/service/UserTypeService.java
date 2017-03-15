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
public class UserTypeService implements BasicService{

    private final UserTypeDAO userTypeDAO;

    public UserTypeService() {
        userTypeDAO = new UserTypeDAO();
    }

    @Override
    public Collection findAll() {
        return userTypeDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return userTypeDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return userTypeDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return userTypeDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return userTypeDAO.delete(pojo);
    }

   
}
