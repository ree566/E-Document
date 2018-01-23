/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.LineType;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class LineTypeDAO extends AbstractDao<Integer, LineType> implements BasicDAO_1<LineType> {

    @Override
    public List<LineType> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public LineType findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(LineType pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(LineType pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(LineType pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
