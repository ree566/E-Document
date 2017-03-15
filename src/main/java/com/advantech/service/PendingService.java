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
public class PendingService implements BasicService{

    private final PendingDAO pendingDAO;

    public PendingService() {
        pendingDAO = new PendingDAO();
    }

    @Override
    public Collection findAll() {
        return pendingDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return pendingDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return pendingDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return pendingDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return pendingDAO.delete(pojo);
    }

   
}
