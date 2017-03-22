/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.Model;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ModelDAO implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return currentSession().createQuery("from Model").list();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return currentSession().load(Model.class, Long.valueOf((int) obj_id));
    }

    public List<Model> findByPrimaryKeys(Integer... id) {
        Criteria criteria = currentSession().createCriteria(Model.class);
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    public Model findByName(String modelName) {
        Query query = currentSession().createQuery("from Model m where m.name = :name");
        query.setParameter("name", modelName);
        return (Model) query.uniqueResult();
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
