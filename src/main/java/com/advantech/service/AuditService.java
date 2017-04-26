/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditAction;
import com.advantech.dao.AuditDAO;
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
public class AuditService implements AuditAction {

    @Autowired
    private AuditDAO auditDAO;

    @Override
    public List findAll(Class clz) {
        return auditDAO.findAll(clz);
    }

    @Override
    public List findByPrimaryKey(Class clz, Object id) {
        return auditDAO.findByPrimaryKey(clz, id);
    }

    @Override
    public Object findByPrimaryKeyAndVersion(Class clz, Object id, int version) {
        return auditDAO.findByPrimaryKeyAndVersion(clz, id, version);
    }

    @Override
    public List<Number> findReversions(Class clz, Object id) {
        return auditDAO.findReversions(clz, id);
    }

    @Override
    public List forEntityAtReversion(Class clz, int version) {
        return auditDAO.forEntityAtReversion(clz, version);
    }

}
