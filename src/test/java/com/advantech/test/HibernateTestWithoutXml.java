/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author Wei.Cheng
 */
public class HibernateTestWithoutXml {

    private SessionFactory sessionFactory;

    @Before
    public void before() {
        sessionFactory = new Configuration()
                .configure(this.getClass().getResource("/hibernate.cfg.xml"))
                .buildSessionFactory();
    }

    @Test
    public void test() {
        Session session = null;
        Transaction txn = null;
        try {
            session = sessionFactory.openSession();
            assertNotNull(session);
            txn = session.beginTransaction();

            List l = session.createSQLQuery("select top 2 * from Worktime").list();
            assertNotNull(l);
            assertEquals(2, l.size());

            HibernateObjectPrinter.print(l);

            txn.commit();
        } catch (HibernateException e) {
            if (txn != null) {
                txn.rollback();
            }
            System.out.println(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
