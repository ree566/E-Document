/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditAction;
import com.advantech.dao.AuditDAO;
import com.advantech.helper.JsonHelper;
import com.advantech.helper.PageInfo;
import java.util.Date;
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

    public List findAll(Class clz, PageInfo info) {
        return auditDAO.findAll(clz, info);
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

    public List findByDate(Class clz, PageInfo info, Date startDate, Date endDate) {
        return this.findByDate(clz, null, info, startDate, endDate);
    }

    public List findByDate(Class clz, Integer id, PageInfo info, Date startDate, Date endDate) {
        return auditDAO.findByDate(clz, id, info, startDate, endDate);
    }
}
