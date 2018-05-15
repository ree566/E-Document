/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.PassStationRecord;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PassStationRecordDAO extends AbstractDao<Integer, PassStationRecord> implements BasicDAO_1<PassStationRecord> {

    @Override
    public List<PassStationRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PassStationRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<PassStationRecord> findByPo(String po) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("po", po))
                .list();
    }

    @Override
    public int insert(PassStationRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<PassStationRecord> l) {
        Session session = super.getSession();
        int row = 0;
        for (PassStationRecord pojo : l) {
            session.save(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

    @Override
    public int update(PassStationRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PassStationRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
