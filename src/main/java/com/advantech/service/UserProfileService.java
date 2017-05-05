/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.UserProfile;
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
public class UserProfileService {

    @Autowired
    private UserProfileDAO userProfileDAO;

    public List<UserProfile> findAll() {
        return (List<UserProfile>) userProfileDAO.findAll();
    }

    public UserProfile findByPrimaryKey(Object obj_id) {
        return (UserProfile) userProfileDAO.findByPrimaryKey(obj_id);
    }

    public int insert(UserProfile userProfile) {
        return userProfileDAO.insert(userProfile);
    }

    public int update(UserProfile userProfile) {
        return userProfileDAO.update(userProfile);
    }

    public int delete(UserProfile userProfile) {
        return userProfileDAO.delete(userProfile);
    }

}
