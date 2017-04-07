/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.SheetView;
import com.advantech.helper.PageInfo;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class SheetViewDAO implements BasicDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        SQLQuery query = currentSession().createSQLQuery("SELECT * FROM Sheet_Main_view");
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        query.setReadOnly(true);
        List results = query.list();
        return results;
    }

    public Collection findAll(PageInfo info) {
        Criteria criteria = currentSession().createCriteria(SheetView.class);
        criteria.setReadOnly(true);

        //Get total row count and reset criteria
        //https://forum.hibernate.org/viewtopic.php?t=951369
        criteria.setProjection(Projections.rowCount());
        info.setMaxNumOfRows(((Long) criteria.uniqueResult()).intValue());
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        String sortIdx = info.getSidx();
        if (sortIdx.length() > 0) {
            if ("asc".equalsIgnoreCase(info.getSord())) {
                criteria.addOrder(Order.asc(sortIdx));
            } else {
                criteria.addOrder(Order.desc(sortIdx));
            }
        }

        if (info.getSearchField() != null) {
            String searchOper = info.getSearchOper();
            String searchField = info.getSearchField();
            Object searchString = info.getSearchString();
            addSearchCriteria(criteria, searchOper, searchField, searchString);
        }

        criteria.setFirstResult((info.getPage() - 1) * info.getRows());
        criteria.setMaxResults(info.getRows());

        return criteria.list();
    }

    private Criteria addSearchCriteria(Criteria criteria, String searchOper, String searchField, Object searchString) {
        switch (searchOper) {
            case "eq":
                criteria.add(Restrictions.eq(searchField, searchString));
                break;
            case "ne":
                criteria.add(Restrictions.ne(searchField, searchString));
                break;
            case "bw":
                criteria.add(Restrictions.like(searchField, (String) searchString, MatchMode.START));
                break;
            case "ew":
                criteria.add(Restrictions.like(searchField, (String) searchString, MatchMode.END));
                break;
            case "cn":
                criteria.add(Restrictions.like(searchField, (String) searchString, MatchMode.ANYWHERE));
                break;
            case "lt":
                criteria.add(Restrictions.lt(searchField, searchString));
                break;
            case "gt":
                criteria.add(Restrictions.gt(searchField, searchString));
                break;
            default:
                break;
        }
        return criteria;
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
