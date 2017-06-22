/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Pending;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PendingDAO extends AbstractDao<Integer, Pending> implements BasicDAO<Pending> {

    @Override
    public List<Pending> findAll() {
        return createEntityCriteria().list();
    }

    public List<Pending> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public Pending findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(Pending obj) {
        getSession().save(obj);
        return 1;
    }

    @Override
    public int update(Pending obj) {
        getSession().update(obj);
        return 1;
    }

    @Override
    public int delete(Pending pojo) {
        getSession().delete(pojo);
        return 1;
    }

}
