/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.CellLineTypeRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class CellLineTypeRecordDAO extends AbstractDao<Integer, CellLineTypeRecord> implements BasicDAO_1<CellLineTypeRecord> {

    @Override
    public List<CellLineTypeRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public CellLineTypeRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(CellLineTypeRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(CellLineTypeRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(CellLineTypeRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
