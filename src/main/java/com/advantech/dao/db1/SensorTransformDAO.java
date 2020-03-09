/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.SensorTransform;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class SensorTransformDAO extends AbstractDao<String, SensorTransform> implements BasicDAO_1<SensorTransform> {

    @Override
    public List<SensorTransform> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public SensorTransform findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    @Override
    public int insert(SensorTransform pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(SensorTransform pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(SensorTransform pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
