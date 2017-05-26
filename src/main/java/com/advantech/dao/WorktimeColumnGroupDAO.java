/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.WorktimeColumnGroup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeColumnGroupDAO implements BasicDAO<WorktimeColumnGroup> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<WorktimeColumnGroup> findAll() {
        return currentSession().createCriteria(WorktimeColumnGroup.class).list();
    }

    @Override
    public WorktimeColumnGroup findByPrimaryKey(Object obj_id) {
        return (WorktimeColumnGroup) currentSession().load(WorktimeColumnGroup.class, (int) obj_id);
    }

    public WorktimeColumnGroup findByUserType(Object obj_id) {
        Criteria criteria = currentSession().createCriteria(WorktimeColumnGroup.class, "w");
        criteria.createAlias("w.unit", "u");
        criteria.add(Restrictions.eq("u.id", obj_id));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (WorktimeColumnGroup) criteria.uniqueResult();
    }

    @Override
    public int insert(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
