/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.PreAssy;
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
public class PreAssyDAO implements BasicDAO<PreAssy> {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<PreAssy> findAll() {
        return currentSession().createCriteria(PreAssy.class).list();
    }

    public List<PreAssy> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), PreAssy.class, info);
    }
 
    @Override
    public PreAssy findByPrimaryKey(Object obj_id) {
        return (PreAssy) currentSession().load(PreAssy.class, (int) obj_id);
    }

    @Override
    public int insert(PreAssy pojo) {
        this.currentSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PreAssy pojo) {
        this.currentSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PreAssy pojo) {
        this.currentSession().delete(pojo);
        return 1;
    }

}
