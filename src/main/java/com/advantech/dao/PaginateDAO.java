/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PaginateDAO {

    private final String fieldPrefix = "f_";

    protected List findAll(Session session, Class clz, PageInfo info) {
        return this.findAll(session, clz, new String[0], info);
    }

    protected List findAll(Session session, Class clz, String[] fetchFields, PageInfo info) {
        Criteria criteria = session.createCriteria(clz);

        for (String field : fetchFields) {
            criteria.createAlias(field, field, JoinType.LEFT_OUTER_JOIN);
        }

        if (info.getSearchField() != null) {
            String searchOper = info.getSearchOper();
            String searchField = info.getSearchField();
            Object searchString = this.autoCaseSearchParam(clz, searchField, info.getSearchString());
            addSearchCriteria(criteria, searchOper, searchField, searchString);
        }

        //Get total row count and reset criteria
        //https://forum.hibernate.org/viewtopic.php?t=951369
        //Set max rows info after "Where" cause(This also create an groupby statement)
        setMaxRowsToInfo(info, criteria);

        //Order after groupby
        String sortIdx = info.getSidx();
        if (sortIdx.length() > 0) {
            if ("asc".equalsIgnoreCase(info.getSord())) {
                criteria.addOrder(Order.asc(sortIdx));
            } else {
                criteria.addOrder(Order.desc(sortIdx));
            }
        }

        criteria.setFirstResult((info.getPage() - 1) * info.getRows());
        criteria.setMaxResults(info.getRows());

        return criteria.list();
    }

    private Object autoCaseSearchParam(Class clz, String searchField, String searchParam) {
        if (searchField.contains(".id")) {
            return Integer.parseInt(searchParam);
        }
        try {
            Field field = clz.getDeclaredField(searchField);
            field.setAccessible(true);
            Class type = field.getType();

            if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.parseInt(searchParam);
            } else if (type.equals(BigDecimal.class)) {
                return new BigDecimal(searchParam);
            } else if (type.equals(java.util.Date.class)) {
                DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
                return fmt.parseDateTime(searchParam).toDate();
            } else {
                return searchParam;
            }
        } catch (NoSuchFieldException | NumberFormatException | SecurityException e) {
            return searchParam;
        }
    }

    private void setMaxRowsToInfo(PageInfo info, Criteria c) {
        c.setProjection(Projections.rowCount());
        info.setMaxNumOfRows(((Long) c.uniqueResult()).intValue());
        //Remove group by statement after get the maxium row count
        c.setProjection(null);
        c.setResultTransformer(Criteria.ROOT_ENTITY);
    }

    private Criteria addSearchCriteria(Criteria criteria, String searchOper, String searchField, Object searchParam) {
        switch (searchOper) {
            case "eq":
                criteria.add(Restrictions.eq(searchField, searchParam));
                break;
            case "ne":
                criteria.add(Restrictions.ne(searchField, searchParam));
                break;
            case "bw":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.START));
                break;
            case "ew":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.END));
                break;
            case "cn":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.ANYWHERE));
                break;
            case "lt":
                criteria.add(Restrictions.lt(searchField, searchParam));
                break;
            case "gt":
                criteria.add(Restrictions.gt(searchField, searchParam));
                break;
            default:
                break;
        }
        return criteria;
    }

}
