/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.model.UserProfile;
import java.util.ArrayList;
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
        return userDAO.findAll();
    }

    public List<User> findAll(PageInfo info) {
        return userDAO.findAll(info);
    }

    public List<User> findAll(PageInfo info, Unit usersUnit) {
        return userDAO.findAll(info, usersUnit);
    }

    public User findByPrimaryKey(Object obj_id) {
        return userDAO.findByPrimaryKey(obj_id);
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

    public List<UserProfile> findUserProfiles(int user_id) {
        List l = new ArrayList();
        User u = this.findByPrimaryKey(user_id);
        l.addAll(u.getUserProfiles());
        return l;
    }

    public List<UserProfile> findUserNotifications(int user_id) {
        List l = new ArrayList();
        User u = this.findByPrimaryKey(user_id);
        l.addAll(u.getUserNotifications());
        return l;
    }

    public List<User> findByUnitName(String unitName) {
        return userDAO.findByUnitName(unitName);
    }

    public List<User> findActive() {
        return userDAO.findActive();
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

    public int resetPsw() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        List<User> l = this.findAll();
        for (User user : l) {
            String encryptPassord = encoder.encode(user.getJobnumber());
            user.setPassword(encryptPassord);
            this.update(user);
        }
        return 1;
    }

}
