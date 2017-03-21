/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Identit;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitService {

    private final IdentitDAO identitDAO;

    public IdentitService() {
        identitDAO = new IdentitDAO();
    }

    public Collection findAll() {
        return identitDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return identitDAO.findByPrimaryKey(obj_id);
    }

    public Identit findByJobnumber(String jobnumber) {
        return identitDAO.findByJobnumber(jobnumber);
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
