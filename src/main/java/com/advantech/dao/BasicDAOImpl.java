/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public abstract class BasicDAOImpl<PK extends Serializable, T> extends HibernateBaseDAO<PK, T> implements BasicDAO<PK, T> {

    @Override
    public List<T> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public T findByPrimaryKey(PK obj_id) {
        return (T) super.getByKey(obj_id);
    }

    public List<T> findByPrimaryKeys(PK... id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    @Override
    public int insert(T pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(T pojo) {
        this.getSession().update(pojo);
        return 1;
    }
    
    public int merge(T pojo) {
        this.getSession().merge(pojo);
        return 1;
    }

    @Override
    public int delete(T pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

    public void flushSession() {
        Session session = this.getSession();
        session.flush();
        session.clear();
    }
}
