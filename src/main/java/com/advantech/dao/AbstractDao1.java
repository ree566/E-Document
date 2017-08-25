/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng
 * @param <PK>
 * @param <T>
 */
public abstract class AbstractDao1<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    @Autowired
    private PaginateDAO1 paginateDAO;

    @Autowired
    private SessionFactory sessionFactory;

    public AbstractDao1() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    protected List<T> getAll(){
        Session session = this.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        Root<T> root = query.from(persistentClass);
        query.select(root);
        Query<T> q = session.createQuery(query);
        List<T> l = q.getResultList();
        return l;
    }

    protected T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    protected List<T> getByPaginateInfo(PageInfo info) {
        Criteria criteria = this.createEntityCriteria();
        return paginateDAO.paginateResult(criteria, persistentClass, info);
    }

    protected List<T> getByPaginateInfo(Criteria criteria, PageInfo info) {
        return paginateDAO.paginateResult(criteria, persistentClass, info);
    }

    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    protected List<T> query() {
        Session session = this.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        
        Root<T> root = query.from(persistentClass);
        
        query.select(root);
        Query<T> q = session.createQuery(query);
        List<T> l = q.getResultList();
        return l;
    }

}
