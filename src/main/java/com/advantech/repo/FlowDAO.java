/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

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
public class FlowDAO extends AbstractDao<Integer, Flow> implements BasicDAO<Flow> {

    @Override
    public List<Flow> findAll() {
        return createEntityCriteria().addOrder(Order.asc("name")).list();
    }

    public List<Flow> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public Flow findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
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

    @Override
    public int insert(Flow pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Flow pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Flow pojo) {
        getSession().delete(pojo);
        return 1;
    }

}
