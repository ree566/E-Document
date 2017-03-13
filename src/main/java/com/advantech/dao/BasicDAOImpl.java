/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BasicDAOImpl {

    private static final Logger log = LoggerFactory.getLogger(BasicDAOImpl.class);
    private final SessionFactory factory = HibernateUtil.getSessionFactory();
    private final Session session;

    public BasicDAOImpl() {
        session = factory.getCurrentSession();
    }

    protected List<Map> findBySQL(String sql, Object... params) {
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        query.scroll(ScrollMode.SCROLL_SENSITIVE);
        query.setReadOnly(true);
        List results = query.list();
        return results;
    }

    protected Collection findAll(String HQL) {
        return session.createQuery(HQL).list();
    }

    protected Object findByPrimaryKey(Class cls, Object obj_id) {
        return session.load(cls, (Serializable) obj_id);
    }

    protected List findMultipleRows(Class pojoClass, String params, Object[] values) {
        return session.createCriteria(pojoClass).add(Restrictions.in(params, values)).list();
    }

    protected List findByHQL(String HQL, Object[] params, Object[] values) {
        return findByHQL(HQL, params, values, null);
    }

    protected List findByHQL(String HQL, Object[] params, Object[] values, Integer resultNum) {
        if (params.length != values.length) {
            log.error("Error, params length not equal values length.");
            return null;
        }
        Query query = resultNum == null ? session.createQuery(HQL) : session.createQuery(HQL).setMaxResults(resultNum);
        for (int i = 0, j = params.length; i < j; i++) {
            query.setParameter((String) params[i], values[i]);
        }
        return query.list();
    }

    public int insert(Object obj) {
        session.save(obj);
        return 1;
    }

    public int update(Object obj) {
        session.update(obj);
        return 1;
    }

    protected int update(String HQL, String[] params, Object[] values) {
        if (params.length != values.length) {
            log.error("Error, params length not equal values length.");
            return 0;
        }
        Query query = session.createQuery(HQL);
        for (int i = 0, j = params.length; i < j; i++) {
            query.setParameter(params[i], values[i]);
        }
        return query.executeUpdate();
    }

    public int delete(Object pojo) {
        session.delete(pojo);
        return 1;
    }

    protected int deleteByHQL(String HQL, String[] params, Object[] values) {
        return this.update(HQL, params, values);
    }

    protected Object[] fillParamToArray(Object... o) {
        return o;
    }

    protected Long integerToLong(Integer i) {
        return Long.valueOf(i);
    }

    protected Long[] integerToLong(Integer... i) {
        Long[] l = new Long[i.length];
        for (int index = 0; index < i.length; index++) {
            l[index] = Long.valueOf(i[index]);
        }
        return l;
    }
}
