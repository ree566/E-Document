/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Type;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TypeDAO extends AbstractDao<Integer, Type> implements BasicDAO<Type> {

    @Override
    public List<Type> findAll() {
        return createEntityCriteria().list();
    }

    public List<Type> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public Type findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<Type> findByPrimaryKeys(Integer... id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    @Override
    public int insert(Type pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Type pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Type pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}
