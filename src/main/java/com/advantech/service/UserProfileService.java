/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.UserProfileRepository;
import com.advantech.model.UserProfile;
import java.util.Arrays;
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
    private UserProfileRepository repo;

    public List<UserProfile> findAll() {
        return repo.findAll();
    }

    public UserProfile findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<UserProfile> findByPrimaryKeys(Integer... ids) {
        return repo.findAllById(Arrays.asList(ids));
    }

    public UserProfile findByType(String name) {
        return repo.findByName(name);
    }

    public int insert(UserProfile userProfile) {
        repo.save(userProfile);
        return 1;
    }

    public int update(UserProfile userProfile) {
        repo.save(userProfile);
        return 1;
    }

    public int delete(UserProfile userProfile) {
        repo.delete(userProfile);
        return 1;
    }

}
