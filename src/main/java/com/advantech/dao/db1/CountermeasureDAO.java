/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.Countermeasure;
import com.advantech.model.db1.CountermeasureEvent;
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
public class CountermeasureDAO extends AbstractDao<Integer, Countermeasure> implements BasicDAO_1<Countermeasure> {

    @Override
    public List<Countermeasure> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public Countermeasure findByPrimaryKey(Object obj_id) {
        Criteria c = super.createEntityCriteria();
        c.setFetchMode("errorCodes", FetchMode.JOIN);
        c.setFetchMode("actionCodes", FetchMode.JOIN);
        c.setFetchMode("countermeasureEvents", FetchMode.JOIN);
        c.add(Restrictions.eq("id", obj_id));
        return (Countermeasure) c.uniqueResult();
    }

    public Countermeasure findByBabAndTypeName(int bab_id, String typeName) {
        Criteria c = super.createEntityCriteria();
        c.createAlias("countermeasureType", "ct");
        c.setFetchMode("errorCodes", FetchMode.JOIN);
        c.setFetchMode("actionCodes", FetchMode.JOIN);
        c.setFetchMode("lastEditor", FetchMode.JOIN);
        c.setFetchMode("countermeasureEvents", FetchMode.JOIN);
        c.setFetchMode("countermeasureSopRecords", FetchMode.JOIN);
        c.add(Restrictions.eq("bab.id", bab_id));
        c.add(Restrictions.eq("ct.name", typeName));
        return (Countermeasure) c.uniqueResult();
    }

    @Override
    public int insert(Countermeasure pojo) {
        getSession().save(pojo);
        updateEvent(pojo, "insert");
        return 1;
    }

    @Override
    public int update(Countermeasure pojo) {
        getSession().update(pojo);
        updateEvent(pojo, "update");
        return 1;
    }

    private void updateEvent(Countermeasure c, String event) {
        getSession().save(new CountermeasureEvent(c, c.getLastEditor(), event));
    }

    @Override
    public int delete(Countermeasure pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
