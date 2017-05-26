/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Floor;
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
public class FloorDAO implements BasicDAO<Floor> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Floor> findAll() {
        return currentSession().createCriteria(Floor.class).list();
    }

    public List<Floor> findByPrimaryKeys(Integer... id) {
        Criteria criteria = currentSession().createCriteria(Floor.class);
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    @Override
    public Floor findByPrimaryKey(Object obj_id) {
        return (Floor) currentSession().load(Floor.class, (int) obj_id);
    }

    @Override
    public int insert(Floor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(Floor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(Floor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
