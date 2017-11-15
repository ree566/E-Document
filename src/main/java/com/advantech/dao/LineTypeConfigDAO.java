/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.LineTypeConfig;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng Read the config from db and put in static variable
 */
@Repository
public class LineTypeConfigDAO extends AbstractDao<Integer, LineTypeConfig> implements BasicDAO_1<LineTypeConfig> {

    @Override
    public List<LineTypeConfig> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public LineTypeConfig findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<LineTypeConfig> findByLineType(int lineType_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("lineType.id", lineType_id));
        return c.list();
    }

    @Override
    public int insert(LineTypeConfig pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(LineTypeConfig pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(LineTypeConfig pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
