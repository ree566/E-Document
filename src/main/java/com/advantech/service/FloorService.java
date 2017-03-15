/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class FloorService implements BasicService{

    private final FloorDAO floorDAO;

    public FloorService() {
        floorDAO = new FloorDAO();
    }

    @Override
    public Collection findAll() {
        return floorDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return floorDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return floorDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return floorDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return floorDAO.delete(pojo);
    }

   
}
