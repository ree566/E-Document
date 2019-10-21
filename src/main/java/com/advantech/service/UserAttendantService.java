/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.UserAttendantDAO;
import com.advantech.model.UserAttendant;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserAttendantService {

    @Autowired
    private UserAttendantDAO dao;

    public List<UserAttendant> findAll() {
        return dao.findAll();
    }

    public List<UserAttendant> findByDate(DateTime d) {
        return dao.findByDate(d);
    }

    public UserAttendant findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(UserAttendant pojo) {
        return dao.insert(pojo);
    }

    public int update(UserAttendant pojo) {
        return dao.update(pojo);
    }

    public int delete(UserAttendant pojo) {
        return dao.delete(pojo);
    }

}
