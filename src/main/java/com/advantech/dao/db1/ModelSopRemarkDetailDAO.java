/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.ModelSopRemarkDetail;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ModelSopRemarkDetailDAO extends AbstractDao<Integer, ModelSopRemarkDetail> implements BasicDAO_1<ModelSopRemarkDetail> {

    @Override
    public List<ModelSopRemarkDetail> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ModelSopRemarkDetail findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<ModelSopRemarkDetail> findByModelName(String modelName) {
        return super.createEntityCriteria()
                .createAlias("modelSopRemark", "mr")
                .add(Restrictions.eq("mr.modelName", modelName))
                .list();
    }

    @Override
    public int insert(ModelSopRemarkDetail pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(ModelSopRemarkDetail pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(ModelSopRemarkDetail pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}
