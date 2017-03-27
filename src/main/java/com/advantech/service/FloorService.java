/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FloorService {

    @Autowired
    private FloorDAO floorDAO;

    public Collection findAll() {
        return floorDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return floorDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Object obj) {
        return floorDAO.insert(obj);
    }

    public int update(Object obj) {
        return floorDAO.update(obj);
    }

    public int delete(Object pojo) {
        return floorDAO.delete(pojo);
    }

}
