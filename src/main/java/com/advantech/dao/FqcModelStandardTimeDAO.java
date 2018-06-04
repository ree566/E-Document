/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.FqcModelStandardTime;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcModelStandardTimeDAO extends AbstractDao<Integer, FqcModelStandardTime> implements BasicDAO_1<FqcModelStandardTime> {

    @Override
    public List<FqcModelStandardTime> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcModelStandardTime findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public List<FqcModelStandardTime> findByName(String modelSeries){
        return super.createEntityCriteria()
                .add(Restrictions.eq("modelNameCategory", modelSeries))
                .list();
    }

    @Override
    public int insert(FqcModelStandardTime pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcModelStandardTime pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcModelStandardTime pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}
