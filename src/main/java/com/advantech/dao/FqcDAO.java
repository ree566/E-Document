/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Fqc;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcDAO extends AbstractDao<Integer, Fqc> implements BasicDAO_1<Fqc> {

    @Override
    public List<Fqc> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public Fqc findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<Fqc> findProcessing() {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.gt("beginTime", new DateTime().withHourOfDay(0).toDate()));
        c.add(Restrictions.isNull("lastUpdateTime"));
        return c.list();
    }
    
    public List<Fqc> findProcessing(int fqcLine_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("fqcLine.id", fqcLine_id));
        c.add(Restrictions.gt("beginTime", new DateTime().withHourOfDay(0).toDate()));
        c.add(Restrictions.isNull("lastUpdateTime"));
        return c.list();
    }

    @Override
    public int insert(Fqc pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Fqc pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Fqc pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
