/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.helper.PageInfo;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }

        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        query.setReadOnly(true);
        List results = query.list();
        return results;
    }

    protected List<Map> findBySQL(PageInfo info, String sql, Object... params) {
        StringBuilder sb = new StringBuilder();
        sql = sb.append(sql).append(" ORDER BY ").append(info.getSidx()).append(" ").append(info.getSord()).toString();

        SQLQuery query = session.createSQLQuery(sql);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }

        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

        info.setMaxNumOfRows(query.list().size());

        query.setFirstResult((info.getPage() - 1) * info.getRows());
        query.setMaxResults(info.getRows());

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

    protected List findByHQL(String HQL, Object[] params, Object[] values) {
        return findByHQL(HQL, params, values, null);
    }

    protected List findByHQL(String HQL, Object[] params, Object[] values, PageInfo info) {
        Query query = session.createQuery(HQL);
        if (params != null && values != null) {
            if (params.length != values.length) {
                log.error("Error, params length not equal values length.");
                return null;
            } else {
                for (int i = 0, j = params.length; i < j; i++) {
                    query.setParameter((String) params[i], values[i]);
                }
            }
        }

        if (info != null) {
            query.setFirstResult((info.getPage() - 1) * info.getRows());
            query.setMaxResults(info.getRows());
        }

        return query.list();
    }

    protected List findByCriteria(Class clz, PageInfo searchInfo) {
        Criteria criteria = session.createCriteria(clz);
        String sortIdx = searchInfo.getSidx();
        if (sortIdx.length() > 0) {
            if ("asc".equalsIgnoreCase(searchInfo.getSord())) {
                criteria.addOrder(Order.asc(sortIdx));
            } else {
                criteria.addOrder(Order.desc(sortIdx));
            }
        }

        criteria.setFirstResult((searchInfo.getPage() - 1) * searchInfo.getRows());
        criteria.setMaxResults(searchInfo.getRows());

        return criteria.list();
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
