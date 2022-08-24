/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FlowDAO extends BasicDAOImpl<Integer, Flow> {

    public List<Flow> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    public List<Flow> findByFlowGroup(FlowGroup flowGroup) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("flowGroup", flowGroup));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    public Flow findByFlowName(String flowName) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("name", flowName));
        return (Flow) criteria.uniqueResult();
    }

}
