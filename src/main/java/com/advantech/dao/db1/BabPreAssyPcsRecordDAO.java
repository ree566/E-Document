/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.BabPreAssyPcsRecord;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabPreAssyPcsRecordDAO extends AbstractDao<Integer, BabPreAssyPcsRecord> implements BasicDAO_1<BabPreAssyPcsRecord> {

    @Override
    public List<BabPreAssyPcsRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabPreAssyPcsRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(BabPreAssyPcsRecord pojo) {
        this.getSession().save(pojo);
        return 1;
    }
    
    public int insert(List<BabPreAssyPcsRecord> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (BabPreAssyPcsRecord a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(BabPreAssyPcsRecord pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabPreAssyPcsRecord pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
