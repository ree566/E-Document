/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.ModelSopRemark;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ModelSopRemarkDAO extends AbstractDao<Integer, ModelSopRemark> implements BasicDAO_1<ModelSopRemark> {

    @Override
    public List<ModelSopRemark> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ModelSopRemark findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(ModelSopRemark pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(ModelSopRemark pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(ModelSopRemark pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
