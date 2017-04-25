/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.MD5Encoder;
import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

    public List<Identit> findAll() {
        return (List<Identit>) identitDAO.findAll();
    }

    public List<Identit> findAll(PageInfo info) {
        return identitDAO.findAll(info);
    }

    public Identit findByPrimaryKey(Object obj_id) {
        return (Identit) identitDAO.findByPrimaryKey(obj_id);
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

    public List<Identit> findByUserTypeName(String userTypeName) {
        return identitDAO.findByUserTypeName(userTypeName);
    }

    public int insert(Identit identit) {
        return identitDAO.insert(identit);
    }

    public int update(Identit identit) {
        return identitDAO.update(identit);
    }

    public int delete(Identit identit) {
        return identitDAO.delete(identit);
    }
}
