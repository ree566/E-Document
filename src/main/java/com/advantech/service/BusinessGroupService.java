/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BusinessGroupDAO;
import com.advantech.model.BusinessGroup;
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
public class BusinessGroupService {

    @Autowired
    private BusinessGroupDAO businessGroupDAO;

    public List<BusinessGroup> findAll() {
        return businessGroupDAO.findAll();
    }

    public BusinessGroup findByPrimaryKey(Object obj_id) {
        return businessGroupDAO.findByPrimaryKey(obj_id);
    }

    public int insert(BusinessGroup pojo) {
        return businessGroupDAO.insert(pojo);
    }

    public int update(BusinessGroup pojo) {
        return businessGroupDAO.update(pojo);
    }

    public int delete(BusinessGroup pojo) {
        return businessGroupDAO.delete(pojo);
    }

}
