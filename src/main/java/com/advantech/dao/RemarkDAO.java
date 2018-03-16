/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Remark;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class RemarkDAO extends AbstractDao<Integer, Remark> implements BasicDAO<Remark> {

    @Override
    public List<Remark> findAll() {
        return createEntityCriteria().list();
    }

    public List<Remark> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public Remark findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(Remark pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Remark pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Remark pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
