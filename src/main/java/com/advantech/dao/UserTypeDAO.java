/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.UserType;
import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class UserTypeDAO implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(UserTypeDAO.class);

    private final SessionFactory factory;
    private final Session session;

    public UserTypeDAO() {
        factory = HibernateUtil.getSessionFactory();
        session = factory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return session.createQuery("from UserType").list();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return session.load(UserType.class, Long.valueOf((int) obj_id));
    }

    @Override
    public int insert(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(Object pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
