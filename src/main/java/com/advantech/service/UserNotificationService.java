/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.User;
import com.advantech.model.UserNotification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.UserNotificationRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserNotificationService {

    @Autowired
    private UserNotificationRepository repo;

    public List<UserNotification> findAll() {
        return repo.findAll();
    }

    public UserNotification findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<User> findUsersByNotification(String name) {
        UserNotification n = repo.findByName(name);
        List l = new ArrayList();
        if (n != null && n.enabled()) {
            l.addAll(n.getUsers());
        }
        return l;
    }

    public int insert(UserNotification userProfile) {
        repo.save(userProfile);
        return 1;
    }

    public int update(UserNotification userProfile) {
        repo.save(userProfile);
        return 1;
    }

    public int delete(UserNotification userProfile) {
        repo.delete(userProfile);
        return 1;
    }

}
