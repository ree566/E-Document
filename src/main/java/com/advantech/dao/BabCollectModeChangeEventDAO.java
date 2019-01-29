/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabCollectModeChangeEvent;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabCollectModeChangeEventDAO extends AbstractDao<Integer, BabCollectModeChangeEvent> implements BasicDAO_1<BabCollectModeChangeEvent> {

    @Override
    public List<BabCollectModeChangeEvent> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabCollectModeChangeEvent findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(BabCollectModeChangeEvent pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BabCollectModeChangeEvent pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabCollectModeChangeEvent pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
