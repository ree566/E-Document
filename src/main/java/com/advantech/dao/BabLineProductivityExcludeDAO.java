/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabLineProductivityExclude;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabLineProductivityExcludeDAO extends AbstractDao<Integer, BabLineProductivityExclude> implements BasicDAO_1<BabLineProductivityExclude> {

    @Override
    public List<BabLineProductivityExclude> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabLineProductivityExclude findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(BabLineProductivityExclude pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BabLineProductivityExclude pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabLineProductivityExclude pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
