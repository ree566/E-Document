/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeDAO extends BasicDAOImpl<Integer, Worktime> {

    public List<Worktime> findAll(PageInfo info) {
        Criteria criteria = createEntityCriteria()
                .setFetchMode("cobots", FetchMode.SELECT)
                .createAlias("bwFields", "bwFields", JoinType.LEFT_OUTER_JOIN);
        List l = getByPaginateInfo(criteria, info);
        return l;
    }

    public Worktime findByModel(String modelName) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("modelName", modelName));
        return (Worktime) criteria.uniqueResult();
    }

    public List<Worktime> findWithFullRelation(PageInfo info) {
        String[] fetchField = {
            "type", "businessGroup", "floor", "workCenter", "preAssy",
            "flowByBabFlowId", "flowByPackingFlowId", "flowByTestFlowId",
            "userBySpeOwnerId", "userByEeOwnerId", "userByQcOwnerId", "userByMpmOwnerId", "remark",
            "pending"
        };

        Criteria criteria = createEntityCriteria();
        for (String field : fetchField) {
            criteria.setFetchMode(field, FetchMode.JOIN);
        }

        criteria.setFetchMode("cobots", FetchMode.SELECT);
//        criteria.createAlias("cobots", "cobots", JoinType.LEFT_OUTER_JOIN);

        String fetchField_c = "bwFields";
        criteria.createAlias(fetchField_c, fetchField_c, JoinType.LEFT_OUTER_JOIN);

        List l = getByPaginateInfo(criteria, info);
        return l;
    }

    public int merge(Worktime pojo) {
        getSession().merge(pojo);
        return 1;
    }

    public int saveOrUpdate(Worktime pojo) {
        getSession().saveOrUpdate(pojo);
        return 1;
    }
   
}
