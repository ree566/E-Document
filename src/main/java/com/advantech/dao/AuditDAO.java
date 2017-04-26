/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class AuditDAO implements AuditAction {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    private AuditReader getReader() {
        return AuditReaderFactory.get(currentSession());
    }

    @Override
    public List findAll(Class clz) {
        AuditQuery q = getReader().createQuery().forRevisionsOfEntity(clz, true, true);
        return q.getResultList();
    }

    @Override
    public Object findByPrimaryKeyAndVersion(Class clz, Object id, int version) {
        return getReader().find(clz, id, 1);
    }

    @Override
    public List findByPrimaryKey(Class clz, Object id) {
        AuditQuery q = getReader().createQuery().forRevisionsOfEntity(clz, true, true);
        q.add(AuditEntity.id().eq(id));
        return q.getResultList();
    }

    @Override
    public List<Number> findReversions(Class clz, Object id) {
        return getReader().getRevisions(clz, id);
    }

    @Override
    public List forEntityAtReversion(Class clz, int version) {
        AuditQuery q = getReader().createQuery().forEntitiesAtRevision(clz, version);
        return q.getResultList();
    }

}
