/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Cobot;
import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class CobotDAO extends AbstractDao<Integer, Cobot> implements BasicDAO<Cobot> {

    @Override
    public List<Cobot> findAll() {
        return createEntityCriteria().list();
    }

    public List<Cobot> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public Cobot findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(Cobot pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Cobot pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Cobot pojo) {
        getSession().delete(pojo);
        return 1;
    }

}
