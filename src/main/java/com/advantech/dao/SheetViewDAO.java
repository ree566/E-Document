/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.HibernateUtil;
import com.advantech.model.SheetView;
import com.advantech.helper.PageInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewDAO implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    private final SessionFactory factory;
    private final Session session;

    public SheetViewDAO() {
        factory = HibernateUtil.getSessionFactory();
        session = factory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        SQLQuery query = session.createSQLQuery("SELECT * FROM Sheet_Main_view");
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        query.setReadOnly(true);
        List results = query.list();
        return results;
    }

    public Collection findAll(PageInfo info) {
        Criteria criteria = session.createCriteria(SheetView.class);
        criteria.setReadOnly(true);

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
            String searchString = info.getSearchString();
            addSearchCriteria(criteria, searchOper, searchField, searchString);
        }

        info.setMaxNumOfRows(criteria.list().size());

        criteria.setFirstResult((info.getPage() - 1) * info.getRows());
        criteria.setMaxResults(info.getRows());

        return criteria.list();
    }

    private Criteria addSearchCriteria(Criteria criteria, String searchOper, String searchField, String searchString) {
        switch (searchOper) {
            case "eq":
                criteria.add(Restrictions.eq(searchField, searchString));
                break;
            case "ne":
                criteria.add(Restrictions.ne(searchField, searchString));
                break;
            case "bw":
                criteria.add(Restrictions.like(searchField, searchString, MatchMode.START));
                break;
            case "bn":
                criteria.add(Restrictions.not(Restrictions.like(searchField, searchString, MatchMode.START)));
                break;
            case "ew":
                criteria.add(Restrictions.like(searchField, searchString, MatchMode.END));
                break;
            case "en":
                criteria.add(Restrictions.not(Restrictions.like(searchField, searchString, MatchMode.END)));
                break;
            case "cn":
                criteria.add(Restrictions.like(searchField, searchString, MatchMode.ANYWHERE));
                break;
            case "nc":
                criteria.add(Restrictions.not(Restrictions.like(searchField, searchString, MatchMode.ANYWHERE)));
                break;
            case "nu":
                criteria.add(Restrictions.isNull(searchField));
                break;
            case "nn":
                criteria.add(Restrictions.not(Restrictions.isNull(searchField)));
                break;
            case "in":
                if (searchString.contains(",")) {
                    String[] params = searchString.trim().split(",");
                    System.out.println(Arrays.toString(params));
                    criteria.add(Restrictions.in(searchField, params));
                } else {
                    criteria.add(Restrictions.eq(searchField, searchString));
                }
                break;
            case "ni":
                if (searchString.contains(",")) {
                    String[] params = searchString.trim().split(",");
                    criteria.add(Restrictions.not(Restrictions.in(searchField, params)));
                } else {
                    criteria.add(Restrictions.not(Restrictions.eq(searchField, searchString)));
                }
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
