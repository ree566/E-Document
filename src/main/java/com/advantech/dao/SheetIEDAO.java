/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.SheetIe;
import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetIEDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetIEDAO.class);

    private final SessionFactory factory;
    private final Session session;

    public SheetIEDAO() {
        factory = HibernateUtil.getSessionFactory();
        session = factory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return session.createQuery("from SheetIe").list();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return session.load(SheetIe.class, Long.valueOf((int) obj_id));
    }

    public String[] getColumnName() {
        return factory.getClassMetadata(SheetIe.class).getPropertyNames();
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
