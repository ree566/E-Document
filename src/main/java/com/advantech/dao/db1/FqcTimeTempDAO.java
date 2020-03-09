/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.FqcTimeTemp;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcTimeTempDAO extends AbstractDao<Integer, FqcTimeTemp> implements BasicDAO_1<FqcTimeTemp> {

    @Override
    public List<FqcTimeTemp> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcTimeTemp findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<FqcTimeTemp> findByFqcIdIn(Integer... fqc_id) {
        return super.createEntityCriteria()
                .add(Restrictions.in("fqc.id", fqc_id))
                .list();
    }

    @Override
    public int insert(FqcTimeTemp pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcTimeTemp pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcTimeTemp pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
