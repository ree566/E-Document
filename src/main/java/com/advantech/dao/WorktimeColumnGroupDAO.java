/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
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
public class WorktimeColumnGroupDAO extends AbstractDao<Integer, WorktimeColumnGroup> implements BasicDAO<WorktimeColumnGroup> {

    @Override
    public List<WorktimeColumnGroup> findAll() {
        return createEntityCriteria().list();
    }

    public List<WorktimeColumnGroup> findAll(PageInfo info) {
        return getByPaginateInfo(info);
    }

    @Override
    public WorktimeColumnGroup findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public WorktimeColumnGroup findByUserType(Object obj_id) {
        Criteria criteria = createEntityCriteria();
        criteria.createAlias("unit", "u");
        criteria.add(Restrictions.eq("u.id", obj_id));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (WorktimeColumnGroup) criteria.uniqueResult();
    }

    @Override
    public int insert(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(WorktimeColumnGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
