/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Pending;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PendingDAO implements BasicDAO<Pending> {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Pending> findAll() {
        return currentSession().createCriteria(Pending.class).list();
    }

    public List<Pending> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), Pending.class, info);
    }

    @Override
    public Pending findByPrimaryKey(Object obj_id) {
        return (Pending) currentSession().load(Pending.class, (int) obj_id);
    }

    @Override
    public int insert(Pending obj) {
        this.currentSession().save(obj);
        return 1;
    }

    @Override
    public int update(Pending obj) {
        this.currentSession().update(obj);
        return 1;
    }

    @Override
    public int delete(Pending pojo) {
        this.currentSession().delete(pojo);
        return 1;
    }

}
