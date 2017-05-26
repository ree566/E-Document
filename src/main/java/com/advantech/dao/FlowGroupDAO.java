/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FlowGroupDAO implements BasicDAO<FlowGroup> {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private PaginateDAO paginateDAO;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<FlowGroup> findAll() {
        return currentSession().createCriteria(FlowGroup.class).list();
    }

    public List<FlowGroup> findAll(PageInfo info) {
        return paginateDAO.findAll(this.currentSession(), FlowGroup.class, info);
    }
 
    @Override
    public FlowGroup findByPrimaryKey(Object obj_id) {
        return (FlowGroup) currentSession().load(FlowGroup.class, (int) obj_id);
    }

    @Override
    public int insert(FlowGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(FlowGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(FlowGroup pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
