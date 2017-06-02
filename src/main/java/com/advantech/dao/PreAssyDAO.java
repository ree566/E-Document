/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.PreAssy;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PreAssyDAO extends AbstractDao<Integer, PreAssy> implements BasicDAO<PreAssy> {

    @Override
    public List<PreAssy> findAll() {
        return createEntityCriteria().list();
    }

    public List<PreAssy> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public PreAssy findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(PreAssy pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PreAssy pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PreAssy pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
