/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.UserNotificationDAO;
import com.advantech.model.User;
import com.advantech.model.UserNotification;
import java.util.ArrayList;
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
public class UserNotificationService {

    @Autowired
    private UserNotificationDAO userNotificationDAO;

    public List<UserNotification> findAll() {
        return userNotificationDAO.findAll();
    }

    public UserNotification findByPrimaryKey(Object obj_id) {
        return userNotificationDAO.findByPrimaryKey(obj_id);
    }

    public List<User> findUsersByNotification(String name) {
        UserNotification n = userNotificationDAO.findByName(name);
        List l = new ArrayList();
        if (n != null && n.enabled()) {
            l.addAll(n.getUsers());
        }
        return l;
    }

    public int insert(UserNotification userProfile) {
        return userNotificationDAO.insert(userProfile);
    }

    public int update(UserNotification userProfile) {
        return userNotificationDAO.update(userProfile);
    }

    public int delete(UserNotification userProfile) {
        return userNotificationDAO.delete(userProfile);
    }

}
