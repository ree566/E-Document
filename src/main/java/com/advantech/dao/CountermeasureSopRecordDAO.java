/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.CountermeasureSopRecord;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
@Repository
public class CountermeasureSopRecordDAO extends AbstractDao<Integer, CountermeasureSopRecord> implements BasicDAO_1<CountermeasureSopRecord> {

    @Override
    public List<CountermeasureSopRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public CountermeasureSopRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public CountermeasureSopRecord findByCountermeasure(int cm_id){
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("countermeasure.id", cm_id));
        return (CountermeasureSopRecord) c.uniqueResult();
    }

    @Override
    public int insert(CountermeasureSopRecord pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(CountermeasureSopRecord pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(CountermeasureSopRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
