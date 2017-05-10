/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PaginateDAO {

    protected List findAll(Session session, Class clz, PageInfo info) {
        Criteria criteria = session.createCriteria(clz);

        if (info.getSearchField() != null) {
            String searchOper = info.getSearchOper();
            String searchField = info.getSearchField();
            String searchString = info.getSearchString();

            if (NumberUtils.isNumber(searchString)) {
                if (searchString.contains(".")) {
                    addSearchCriteria(criteria, searchOper, searchField, Double.parseDouble(searchString));
                } else {
                    addSearchCriteria(criteria, searchOper, searchField, Integer.parseInt(searchString));
                }
            } else if (isValidDate(searchString)) {
                DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
                addSearchCriteria(criteria, searchOper, searchField, fmt.parseDateTime(searchString).toDate());
            } else {
                addSearchCriteria(criteria, searchOper, searchField, searchString);
            }
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

    private boolean isValidDate(String date) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
            fmt.parseDateTime((String) date);
            return true;
        } catch (Exception e) {
            return false;
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
