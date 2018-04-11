/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.TestRecord;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TestRecordDAO extends AbstractDao<Integer, TestRecord> implements BasicDAO_1<TestRecord> {

    @Value("${HIBERNATE.JDBC.BATCHSIZE}")
    private Integer batchSize;

    @Override
    public List<TestRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public TestRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<TestRecord> findByDate(DateTime sD, DateTime eD) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.setFetchMode("testTable", FetchMode.JOIN);
        c.add(Restrictions.between("lastUpdateTime", sD.toDate(), eD.toDate()));
        return c.list();
    }

    @Override
    public int insert(TestRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<TestRecord> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (TestRecord a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(TestRecord pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(TestRecord pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void flushIfReachFetchSize(Session session, int currentRow) {
        if (currentRow % batchSize == 0 && currentRow > 0) {
            session.flush();
            session.clear();
        }
    }

}
