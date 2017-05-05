/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeDAO implements BasicDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return currentSession().createCriteria(Worktime.class).list();
    }

    public List<Worktime> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), Worktime.class, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return currentSession().load(Worktime.class, (int) obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... id) {
        Criteria criteria = currentSession().createCriteria(Worktime.class);
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    public Worktime findByModel(String modelName) {
        Criteria criteria = currentSession().createCriteria(Worktime.class);
        criteria.add(Restrictions.eq("modelName", modelName));
        return (Worktime) criteria.uniqueResult();
    }

    public String[] getColumnName() {
        return sessionFactory.getClassMetadata(Worktime.class).getPropertyNames();
    }

    @Override
    public int insert(Object obj) {
        currentSession().save(obj);
        return 1;
    }
    
    public int merge(Object obj) {
        currentSession().merge(obj);
        return 1;
    }

    public int saveOrUpdate(Object obj) {
        currentSession().saveOrUpdate(obj);
        return 1;
    }

    @Override
    public int update(Object obj) {
        currentSession().update(obj);
        return 1;
    }

    @Override
    public int delete(Object pojo) {
        currentSession().delete(pojo);
        return 1;
    }
}
