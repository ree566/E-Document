/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Type;
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
public class TypeDAO implements BasicDAO<Type> {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Type> findAll() {
        return currentSession().createCriteria(Type.class).list();
    }

    public List<Type> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), Type.class, info);
    }

    @Override
    public Type findByPrimaryKey(Object obj_id) {
        return (Type) currentSession().load(Type.class, (int) obj_id);
    }
    
    public List<Type> findByPrimaryKeys(Integer... id) {
        Criteria criteria = currentSession().createCriteria(Type.class);
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    @Override
    public int insert(Type pojo) {
        this.currentSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Type pojo) {
        this.currentSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Type pojo) {
        this.currentSession().delete(pojo);
        return 1;
    }

}
