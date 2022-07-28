/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.WorktimeColumnGroup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeColumnGroupDAO extends BasicDAOImpl<Integer, WorktimeColumnGroup> {

    public List<WorktimeColumnGroup> findAll(PageInfo info) {
        return getByPaginateInfo(info);
    }

    public WorktimeColumnGroup findByUnit(int obj_id) {
        Criteria criteria = createEntityCriteria();
        criteria.createAlias("unit", "u");
        criteria.add(Restrictions.eq("u.id", obj_id));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (WorktimeColumnGroup) criteria.uniqueResult();
    }

}
