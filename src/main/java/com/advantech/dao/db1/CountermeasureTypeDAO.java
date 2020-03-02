/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.CountermeasureType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
@Repository
public class CountermeasureTypeDAO extends AbstractDao<Integer, CountermeasureType> implements BasicDAO_1<CountermeasureType> {

    @Override
    public List<CountermeasureType> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public CountermeasureType findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public CountermeasureType findByName(String name) {
        return (CountermeasureType) super.createEntityCriteria()
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }

    @Override
    public int insert(CountermeasureType pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(CountermeasureType pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(CountermeasureType pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
