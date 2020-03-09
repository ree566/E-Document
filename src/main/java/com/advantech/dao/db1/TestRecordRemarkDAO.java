/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.TestRecordRemark;
import java.util.List;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TestRecordRemarkDAO extends AbstractDao<Integer, TestRecordRemark> implements BasicDAO_1<TestRecordRemark> {

    @Override
    public List<TestRecordRemark> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public TestRecordRemark findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public TestRecordRemark findByTestRecord(int recordId){
        return (TestRecordRemark) super.createEntityCriteria()
                .add(Restrictions.eq("testRecord.id", recordId))
                .setFetchMode("user", FetchMode.JOIN)
                .uniqueResult();
    }

    @Override
    public int insert(TestRecordRemark pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(TestRecordRemark pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(TestRecordRemark pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
