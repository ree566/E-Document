/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.FqcLoginRecord;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcLoginRecordDAO extends AbstractDao<Integer, FqcLoginRecord> implements BasicDAO_1<FqcLoginRecord> {

    @Override
    public List<FqcLoginRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcLoginRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public FqcLoginRecord findByFqcLine(int fqcLine_id) {
        return (FqcLoginRecord) super.createEntityCriteria()
                .add(Restrictions.eq("fqcLine.id", fqcLine_id))
                .uniqueResult();
    }

    @Override
    public int insert(FqcLoginRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcLoginRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcLoginRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
