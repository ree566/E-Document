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
public class WorktimeDAO implements BasicDAO<Worktime> {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Worktime> findAll() {
        return currentSession().createCriteria(Worktime.class).list();
    }

    public List<Worktime> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), Worktime.class, info);
    }

    @Override
    public Worktime findByPrimaryKey(Object obj_id) {
        return (Worktime) currentSession().load(Worktime.class, (int) obj_id);
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
    public int insert(Worktime pojo) {
        currentSession().save(pojo);
        return 1;
    }

    public int merge(Worktime pojo) {
        currentSession().merge(pojo);
        return 1;
    }

    public int saveOrUpdate(Worktime pojo) {
        currentSession().saveOrUpdate(pojo);
        return 1;
    }

    @Override
    public int update(Worktime pojo) {
        currentSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Worktime pojo) {
        currentSession().delete(pojo);
        return 1;
    }
}
