/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.WorkCenter;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorkCenterDAO extends AbstractDao<Integer, WorkCenter> implements BasicDAO<WorkCenter> {

    @Override
    public List<WorkCenter> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public WorkCenter findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<WorkCenter> findByBusinessGroup(int businessGroupId) {
        return super.createEntityCriteria()
                .createAlias("businessGroup", "b")
                .add(Restrictions.eq("b.id", businessGroupId))
                .list();
    }

    @Override
    public int insert(WorkCenter pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(WorkCenter pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(WorkCenter pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
