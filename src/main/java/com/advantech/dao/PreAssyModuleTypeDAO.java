/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.PreAssyModuleType;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PreAssyModuleTypeDAO extends AbstractDao<Integer, PreAssyModuleType> implements BasicDAO_1<PreAssyModuleType> {

    @Override
    public List<PreAssyModuleType> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PreAssyModuleType findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(PreAssyModuleType pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PreAssyModuleType pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PreAssyModuleType pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
