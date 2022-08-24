/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FloorService extends BasicServiceImpl<Integer, Floor> {

    @Autowired
    private FloorDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

}
