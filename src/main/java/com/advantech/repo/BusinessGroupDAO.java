/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.BusinessGroup;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BusinessGroupDAO extends AbstractDao<Integer, BusinessGroup> implements BasicDAO<BusinessGroup> {

    @Override
    public List<BusinessGroup> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public BusinessGroup findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(BusinessGroup pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BusinessGroup pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BusinessGroup pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
