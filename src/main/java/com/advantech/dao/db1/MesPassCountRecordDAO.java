/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.MesPassCountRecord;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class MesPassCountRecordDAO extends AbstractDao<Integer, MesPassCountRecord> implements BasicDAO_1<MesPassCountRecord> {

    @Override
    public List<MesPassCountRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public MesPassCountRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(MesPassCountRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }
   
    public int insert(List<MesPassCountRecord> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (MesPassCountRecord a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }
    
    @Override
    public int update(MesPassCountRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(MesPassCountRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
