/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
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
public class UserNotificationService extends BasicServiceImpl<Integer, UserNotification> {

    @Autowired
    private UserNotificationDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<User> findUsersByNotification(String name) {
        UserNotification n = dao.findByName(name);
        List l = new ArrayList();
        if (n != null && n.enabled()) {
            l.addAll(n.getUsers());
        }
        return l;
    }

}
