/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.AlarmTestAction;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class AlarmTestActionDAO extends AbstractDao_1<String, AlarmTestAction> implements BasicDAO_1<AlarmTestAction>{

    @Override
    public List<AlarmTestAction> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public AlarmTestAction findByPrimaryKey(Object obj_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int insert(AlarmTestAction pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(AlarmTestAction pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(AlarmTestAction pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
    
}
