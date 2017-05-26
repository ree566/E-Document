/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserProfile;
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
public class UserProfileDAO implements BasicDAO<UserProfile> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<UserProfile> findAll() {
        return currentSession().createCriteria(UserProfile.class).list();
    }

    @Override
    public UserProfile findByPrimaryKey(Object obj_id) {
        return (UserProfile) currentSession().load(UserProfile.class, (int) obj_id);
    }
    
    public UserProfile findByType(String typeName){
        Criteria c = currentSession().createCriteria(UserProfile.class);
        c.add(Restrictions.eq("type", typeName));
        return (UserProfile) c.uniqueResult();
    }

    @Override
    public int insert(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
