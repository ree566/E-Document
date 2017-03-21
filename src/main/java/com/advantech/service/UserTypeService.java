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
public class UserTypeService {

    private final UserTypeDAO userTypeDAO;

    public UserTypeService() {
        userTypeDAO = new UserTypeDAO();
    }

    public Collection findAll() {
        return userTypeDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return userTypeDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Object obj) {
        return userTypeDAO.insert(obj);
    }

    public int update(Object obj) {
        return userTypeDAO.update(obj);
    }

    public int delete(Object pojo) {
        return userTypeDAO.delete(pojo);
    }

}
