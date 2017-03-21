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

/**
 *
 * @author Wei.Cheng
 */
public class ModelDAO implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);
    private final SessionFactory factory;
    private final Session session;

    public ModelDAO() {
        factory = HibernateUtil.getSessionFactory();
        session = factory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return session.createQuery("from Model").list();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return session.load(Model.class, Long.valueOf((int) obj_id));
    }

    public List<Model> findByPrimaryKeys(Integer... id) {
        Criteria criteria = session.createCriteria(Model.class);
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    public Model findByName(String modelName) {
        Query query = session.createQuery("from Model m where m.name = :name");
        query.setParameter("name", modelName);
        return (Model) query.uniqueResult();
    }

    @Override
    public int insert(Object obj) {
        session.save(obj);
        return 1;
    }

    @Override
    public int update(Object obj) {
        session.update(obj);
        return 1;
    }

    @Override
    public int delete(Object pojo) {
        session.delete(pojo);
        return 1;
    }

}
