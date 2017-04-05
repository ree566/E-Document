/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.MD5Encoder;
import com.advantech.model.Identit;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (i == null) {
            return null;
        }
        
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

    public void encryptPassord() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        List<Identit> l = (List<Identit>) this.findAll();
        for (Identit i : l) {
            String password = i.getPassword();
            if (password == null) {
                continue;
            }
            i.setPassword(MD5Encoder.toMD5("Hello world!" + password));
            this.update(i);
        }
    }

}
