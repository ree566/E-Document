/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

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
public class WorktimeDAO extends AbstractDao<Integer, Worktime> implements BasicDAO<Worktime> {

    @Override
    public List<Worktime> findAll() {
        return createEntityCriteria().list();
    }

    public List<Worktime> findAll(PageInfo info) {
        String fetchField = "bwFields";
        Criteria criteria = createEntityCriteria();
        criteria.createAlias(fetchField, fetchField, JoinType.LEFT_OUTER_JOIN);
        List l = getByPaginateInfo(criteria, info);
        return l;
    }

    @Override
    public Worktime findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    public Worktime findByModel(String modelName) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("modelName", modelName));
        return (Worktime) criteria.uniqueResult();
    }

    public List<Worktime> findWithFullRelation(PageInfo info) {
        String[] fetchField = {
            "type", "businessGroup", "floor", "pending", "preAssy",
            "flowByBabFlowId", "flowByPackingFlowId", "flowByTestFlowId",
            "userBySpeOwnerId", "userByEeOwnerId", "userByQcOwnerId"
        };

        Criteria criteria = createEntityCriteria();
        for (String field : fetchField) {
            criteria.setFetchMode(field, FetchMode.JOIN);
        }

        String fetchField_c = "bwFields";
        criteria.createAlias(fetchField_c, fetchField_c, JoinType.LEFT_OUTER_JOIN);

        List l = getByPaginateInfo(criteria, info);
        return l;
    }

    @Override
    public int insert(Worktime pojo) {
        getSession().save(pojo);
        return 1;
    }

    public int merge(Worktime pojo) {
        getSession().merge(pojo);
        return 1;
    }

    public int saveOrUpdate(Worktime pojo) {
        getSession().saveOrUpdate(pojo);
        return 1;
    }

    @Override
    public int update(Worktime pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Worktime pojo) {
        getSession().delete(pojo);
        return 1;
    }

    public void flushSession() {
        Session session = this.getSession();
        session.flush();
        session.clear();
    }
}
