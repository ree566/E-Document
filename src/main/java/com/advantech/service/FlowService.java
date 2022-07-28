/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
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
public class FlowService extends BasicServiceImpl<Integer, Flow> {

    @Autowired
    private FlowDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    @Autowired
    private FlowGroupService flowGroupService;

    public List<Flow> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public Flow findByFlowName(String flowName) {
        return dao.findByFlowName(flowName);
    }

    public List<Flow> findByFlowGroup(int flowGroupId) {
        FlowGroup fg = flowGroupService.findByPrimaryKey(flowGroupId);
        return dao.findByFlowGroup(fg);
    }

    public List<Flow> findByParent(Integer parent_id) {
        List l = new ArrayList();
        Flow f = this.findByPrimaryKey(parent_id);
        l.addAll(f.getFlowsForTestFlowId());
        return l;
    }

    public List<Flow> findFlowWithSub() {
        List<Flow> l = this.findAll();
        for (Flow f : l) {
            Hibernate.initialize(f.getFlowsForTestFlowId());
        }
        return l;
    }

    public int addSub(int parentFlowId, List<Integer> subFlowId) {
        Flow f = this.findByPrimaryKey(parentFlowId);
        Set<Flow> subFlows = f.getFlowsForTestFlowId();
        for (Integer id : subFlowId) {
            subFlows.add(this.findByPrimaryKey(id));
        }
        this.update(f);
        return 1;
    }

    public int deleteSub(int parentFlowId, List<Integer> subFlowId) {
        Flow f = this.findByPrimaryKey(parentFlowId);
        Set<Flow> subFlows = f.getFlowsForTestFlowId();

        Iterator it = subFlows.iterator();

        while (it.hasNext()) {
            Flow subFlow = (Flow) it.next();
            if (subFlowId.contains(subFlow.getId())) {
                it.remove();
            }
        }

        return 1;
    }
    
    public int delete(int id) {
        Flow flow = this.findByPrimaryKey(id);
        return dao.delete(flow);
    }

}
