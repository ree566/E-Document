/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.FqcLine;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcLineDAO extends AbstractDao<Integer, FqcLine> implements BasicDAO_1<FqcLine> {

    @Override
    public List<FqcLine> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcLine findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<FqcLine> findBySitefloor(int floor_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("floor.id", floor_id));
        return c.list();
    }

    public List<FqcLine> findBySitefloor(String floor_name) {
        Criteria c = super.createEntityCriteria();
        c.createAlias("floor", "f");
        c.add(Restrictions.eq("f.name", floor_name));
        c.addOrder(Order.asc("name"));
        return c.list();
    }

    @Override
    public int insert(FqcLine pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcLine pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcLine pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
