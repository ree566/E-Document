/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.helper.PageInfo;
import com.advantech.model.User;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public List<User> findAll() {
        return (List<User>) userDAO.findAll();
    }

    public List<User> findAll(PageInfo info) {
        return userDAO.findAll(info);
    }

    public User findByPrimaryKey(Object obj_id) {
        return (User) userDAO.findByPrimaryKey(obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        User i = userDAO.findByJobnumber(jobnumber);

        if (i == null) {
            return null;
        }

        //Initialize the lazy loading relative object
        Hibernate.initialize(i.getUnit());
        Hibernate.initialize(i.getFloor());
        Hibernate.initialize(i.getUserProfiles());

        return i;
    }

    public List<User> findByUnitName(String unitName) {
        return userDAO.findByUnitName(unitName);
    }

    public int insert(User user) {
        return userDAO.insert(user);
    }

    public int update(User user) {
        return userDAO.update(user);
    }

    public int delete(int id) {
        User user = this.findByPrimaryKey(id);
        return userDAO.delete(user);
    }
   
}
