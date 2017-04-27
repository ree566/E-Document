/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class AuditDAO implements AuditAction {

    private final boolean selectedEntitiesOnly = false;
    private final boolean selectDeletedEntities = true;
    private final Set<String> auditColumnNames = new HashSet<>(Arrays.asList(
            new String[]{"REV", "username"}
    ));

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
        AuditQuery q = getReader().createQuery().forRevisionsOfEntity(clz, selectedEntitiesOnly, selectDeletedEntities);
        return q.getResultList();
    }

    public List findAll(Class clz, PageInfo info) {
        AuditQuery q = getReader().createQuery().forRevisionsOfEntity(clz, selectedEntitiesOnly, selectDeletedEntities);
        info.setMaxNumOfRows(((Long) q.addProjection(AuditEntity.id().count()).getSingleResult()).intValue());

        q = getReader().createQuery().forRevisionsOfEntity(clz, selectedEntitiesOnly, selectDeletedEntities)
                .setMaxResults(info.getRows())
                .setFirstResult((info.getPage() - 1) * info.getRows());
        String sortIndex = info.getSidx();
        AuditProperty property = auditColumnNames.contains(sortIndex) ? AuditEntity.revisionProperty(sortIndex) : AuditEntity.property(sortIndex);

        q.addOrder(info.getSord().equals("asc") ? property.asc() : property.desc());
        return q.getResultList();
    }

    @Override
    public Object findByPrimaryKeyAndVersion(Class clz, Object id, int version) {
        return getReader().find(clz, id, version);
    }

    @Override
    public List findByPrimaryKey(Class clz, Object id) {
        AuditQuery q = getReader().createQuery().forRevisionsOfEntity(clz, selectedEntitiesOnly, selectDeletedEntities);
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
