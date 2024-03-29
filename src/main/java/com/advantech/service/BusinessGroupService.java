/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
import com.advantech.dao.BusinessGroupDAO;
import com.advantech.model.BusinessGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class BusinessGroupService extends BasicServiceImpl<Integer, BusinessGroup> {

    @Autowired
    private BusinessGroupDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return dao;
    }

}
