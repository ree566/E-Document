/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.MesLine;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class MesLineDAO extends AbstractDao<Integer, MesLine> implements BasicDAO_1<MesLine> {

    @Override
    public List<MesLine> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public MesLine findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(MesLine pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(MesLine pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(MesLine pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
