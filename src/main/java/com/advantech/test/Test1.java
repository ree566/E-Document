/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.Worktime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Wei.Cheng
 */
public class Test1 {

    public static void main(String arg0[]) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.getCurrentSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Worktime.class);
            criteria.add(Restrictions.eq("modelName", "GAVV"));
            Worktime w = (Worktime) criteria.uniqueResult();


            print(w);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            factory.close();
        }
    }

    private static void print(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Hibernate4Module hbm = new Hibernate4Module();
        hbm.enable(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        mapper.registerModule(hbm);
        System.out.println(mapper.writeValueAsString(obj));
    }

}
