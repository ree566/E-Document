/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.TestPassStationDetail;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TestPassStationDetailDAO extends AbstractDao_1<Integer, TestPassStationDetail> implements BasicDAO_1<TestPassStationDetail> {

    @Override
    public List<TestPassStationDetail> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public TestPassStationDetail findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<TestPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return super.createEntityCriteria()
                .add(Restrictions.between("createDate", sD.toDate(), eD.toDate()))
                .list();
    }

    @Override
    public int insert(TestPassStationDetail pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<TestPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (TestPassStationDetail pojo : l) {
            session.save(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

    @Override
    public int update(TestPassStationDetail pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(TestPassStationDetail pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

    public int delete(List<TestPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (TestPassStationDetail pojo : l) {
            session.delete(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

}
