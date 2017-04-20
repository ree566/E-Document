/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import java.util.List;
import org.hibernate.Hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FlowService {

    @Autowired
    private FlowDAO flowDAO;

    public List<Flow> findAll() {
        return (List<Flow>) flowDAO.findAll();
    }

    public List<Flow> findAll(PageInfo info) {
        List<Flow> l = flowDAO.findAll(info);
        return l;
    }

    public Flow findByPrimaryKey(Object obj_id) {
        return (Flow) flowDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Flow flow) {
        return flowDAO.insert(flow);
    }

    public int update(Flow flow) {
        return flowDAO.update(flow);
    }

    public int delete(Flow flow) {
        return flowDAO.delete(flow);
    }

}
