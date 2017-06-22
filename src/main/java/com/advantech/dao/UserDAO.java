/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.security.State;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserDAO extends AbstractDao<Integer, User> implements BasicDAO<User> {

    @Override
    public List<User> findAll() {
        return createEntityCriteria().list();
    }

    public List<User> findAll(PageInfo info) {
        return getByPaginateInfo(info);
    }

    public List<User> findAll(PageInfo info, Unit usersUnit) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("unit.id", usersUnit.getId()));
        
        String searchField = info.getSearchField();
        String searchString = info.getSearchString();
        if (searchField != null && !"".equals(searchField) && searchString != null && !"".equals(searchString)) {
            criteria.add(Restrictions.eq(info.getSearchField(), info.getSearchString()));
        }
        criteria.setFirstResult((info.getPage() - 1) * info.getRows());
        criteria.setMaxResults(info.getRows());
        return criteria.list();
    }

    @Override
    public User findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("jobnumber", jobnumber));
        User i = (User) criteria.uniqueResult();
        return i;
    }

    public List<User> findByUnitName(String userTypeName) {
        Criteria criteria = createEntityCriteria();
        criteria.createAlias("unit", "u");
        criteria.add(Restrictions.eq("u.name", userTypeName));
        criteria.add(Restrictions.eq("state", State.ACTIVE.getName()));
        criteria.addOrder(Order.asc("username"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<User> findActive() {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("state", State.ACTIVE.getName()));
        return criteria.list();
    }

    @Override
    public int insert(User pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(User pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(User pojo) {
        getSession().delete(pojo);
        return 1;
    }

}
