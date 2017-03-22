/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Identit;
import java.util.Collection;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class IdentitService {

    @Autowired
    private IdentitDAO identitDAO;

    public Collection findAll() {
        return identitDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return identitDAO.findByPrimaryKey(obj_id);
    }

    public Identit findByJobnumber(String jobnumber) {
        Identit i = identitDAO.findByJobnumber(jobnumber);
        //Initialize the lazy loading relative object
        Hibernate.initialize(i.getUserType());
        Hibernate.initialize(i.getFloor());
        return i;
    }

    public int insert(Object obj) {
        return identitDAO.insert(obj);
    }

    public int update(Object obj) {
        return identitDAO.update(obj);
    }

    public int delete(Object pojo) {
        return identitDAO.delete(pojo);
    }

}
