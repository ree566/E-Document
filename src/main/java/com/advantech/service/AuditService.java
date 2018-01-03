/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditAction;
import com.advantech.dao.AuditDAO;
import com.advantech.jqgrid.PageInfo;
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
    public List<Number> findRevisions(Class clz, Object id) {
        return auditDAO.findRevisions(clz, id);
    }

    @Override
    public List forEntityAtRevision(Class clz, int version) {
        return auditDAO.forEntityAtRevision(clz, version);
    }

    public List findModifiedAtRevision(Class clz, int version) {
        return auditDAO.findModifiedAtRevision(clz, version);
    }

    public List findByDate(Class clz, PageInfo info, Date startDate, Date endDate) {
        return this.findByDate(clz, null, info, startDate, endDate);
    }

    public List findByDate(Class clz, Integer id, PageInfo info, Date startDate, Date endDate) {
        return auditDAO.findByDate(clz, id, info, startDate, endDate);
    }

    public Number findLastRevisions(Class clz) {
        return auditDAO.findLastRevisions(clz, null);
    }

    public Number findLastRevisions(Class clz, Object id) {
        return auditDAO.findLastRevisions(clz, id);
    }

    public boolean isFieldChangedAtLastRevision(Class clz, Object id, String fieldName) {
        return auditDAO.isFieldChangedAtLastRevision(clz, id, fieldName);
    }

    public Object findLastStatusBeforeUpdate(Class clz, Object id) {
        return auditDAO.findLastStatusBeforeUpdate(clz, id);
    }

    public boolean isFieldChangedInTime(Class clz, Object id, List<String> fieldNames, Date startDate, Date endDate) {
        return auditDAO.isFieldChangedInTime(clz, id, fieldNames, startDate, endDate);
    }

}
