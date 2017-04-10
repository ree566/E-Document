/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Pending;
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
public class PendingService {

    @Autowired
    private PendingDAO pendingDAO;

    public List<Pending> findAll() {
        return (List<Pending>) pendingDAO.findAll();
    }

    public Pending findByPrimaryKey(Object obj_id) {
        return (Pending) pendingDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Pending pending) {
        return pendingDAO.insert(pending);
    }

    public int update(Pending pending) {
        return pendingDAO.update(pending);
    }

    public int delete(Pending pending) {
        return pendingDAO.delete(pending);
    }

}
