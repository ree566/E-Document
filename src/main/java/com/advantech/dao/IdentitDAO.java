/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.Identit;
import java.util.Collection;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class IdentitDAO implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(IdentitDAO.class);
    
    @Autowired
    private SessionFactory sessionFactory;

    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return currentSession().createQuery("from Identit").list();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return currentSession().load(Identit.class, Long.valueOf((int) obj_id));
    }

    public Identit findByJobnumber(String jobnumber) {
        String HQL = "from Identit i where i.jobnumber = :jobnumber";
        Query query = currentSession().createQuery(HQL);
        query.setParameter("jobnumber", jobnumber);
        Identit i = (Identit) query.uniqueResult();
        return i;
    }

    @Override
    public int insert(Object obj) {
        currentSession().save(obj);
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
