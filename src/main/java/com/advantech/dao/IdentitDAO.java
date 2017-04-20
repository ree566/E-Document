/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class IdentitDAO extends PaginateDAO implements BasicDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Collection findAll() {
        return currentSession().createQuery("from Identit i order by i.name").list();
    }

    public List<Identit> findAll(PageInfo info) {
        return super.findAll(this.currentSession(), Identit.class, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return currentSession().load(Identit.class, (int) obj_id);
    }

    public Identit findByJobnumber(String jobnumber) {
        String HQL = "from Identit i where i.jobnumber = :jobnumber";
        Query query = currentSession().createQuery(HQL);
        query.setParameter("jobnumber", jobnumber);
        Identit i = (Identit) query.uniqueResult();
        return i;
    }

    public List<Identit> findByUserTypeName(String userTypeName) {
        Criteria criteria = currentSession().createCriteria(Identit.class, "i");
        criteria.createAlias("i.userType", "u");
        criteria.add(Restrictions.eq("u.name", userTypeName));
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
