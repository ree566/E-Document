/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.AlarmBabAction;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class AlarmBabActionDAO extends AbstractDao<String, AlarmBabAction> implements BasicDAO_1<AlarmBabAction> {

    @Override
    public List<AlarmBabAction> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public AlarmBabAction findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    @Override
    public int insert(AlarmBabAction pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(AlarmBabAction pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(AlarmBabAction pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
