/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.BabPassStationErrorRecord;
import com.advantech.model.db1.BabPassStationRecord;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabPassStationErrorRecordDAO extends AbstractDao_1<Integer, BabPassStationErrorRecord> implements BasicDAO_1<BabPassStationErrorRecord> {

    @Override
    public List<BabPassStationErrorRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabPassStationErrorRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public BabPassStationErrorRecord findByBabPassStationRecord(BabPassStationRecord rec){
        return (BabPassStationErrorRecord) super.createEntityCriteria()
                .add(Restrictions.eq("babPassStationRecord", rec))
                .uniqueResult();
    }

    @Override
    public int insert(BabPassStationErrorRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BabPassStationErrorRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabPassStationErrorRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
