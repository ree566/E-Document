/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelInfoDAO labelInfoDAO;

    public Collection findAll() {
        return labelInfoDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return labelInfoDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Object obj) {
        return labelInfoDAO.insert(obj);
    }

    public int update(Object obj) {
        return labelInfoDAO.update(obj);
    }

    public int delete(Object pojo) {
        return labelInfoDAO.delete(pojo);
    }

}
