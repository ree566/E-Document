/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.User;
import com.advantech.security.State;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserDAO implements BasicDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return currentSession().createCriteria(User.class).addOrder(Order.asc("username")).list();
    }

    public List<User> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), User.class, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        //Use get for not lazy loading, load for lazy loading.
        return currentSession().get(User.class, (int) obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("jobnumber", jobnumber));
        User i = (User) criteria.uniqueResult();
        return i;
    }

    public List<User> findByUnitName(String userTypeName) {
        Criteria criteria = currentSession().createCriteria(User.class, "i");
        criteria.createAlias("i.unit", "u");
        criteria.add(Restrictions.eq("u.name", userTypeName));
//        criteria.add(Restrictions.eq("i.state", State.ACTIVE.getName()));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public int insert(Object obj) {
        currentSession().save(obj);
        return 1;
    }

    @Override
    public int update(Object obj) {
        currentSession().update(obj);
        return 1;
    }

    @Override
    public int delete(Object pojo) {
        currentSession().delete(pojo);
        return 1;
    }

}
