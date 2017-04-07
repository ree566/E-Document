/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.UserType;
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
public class UserTypeService {

    @Autowired
    private UserTypeDAO userTypeDAO;

    public List<UserType> findAll() {
        return (List<UserType>) userTypeDAO.findAll();
    }

    public UserType findByPrimaryKey(Object obj_id) {
        return (UserType) userTypeDAO.findByPrimaryKey(obj_id);
    }

    public int insert(UserType userType) {
        return userTypeDAO.insert(userType);
    }

    public int update(UserType userType) {
        return userTypeDAO.update(userType);
    }

    public int delete(UserType userType) {
        return userTypeDAO.delete(userType);
    }

}
