/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.ModelSopRemarkEvent;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ModelSopRemarkEventDAO extends AbstractDao_1<Integer, ModelSopRemarkEvent> implements BasicDAO_1<ModelSopRemarkEvent> {

    @Override
    public List<ModelSopRemarkEvent> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ModelSopRemarkEvent findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(ModelSopRemarkEvent pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(ModelSopRemarkEvent pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(ModelSopRemarkEvent pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}
