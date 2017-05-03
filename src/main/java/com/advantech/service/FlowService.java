/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    public List<Flow> findByParent(Object parent_id) {
        List l = new ArrayList();
        Flow f = this.findByPrimaryKey(parent_id);
        l.addAll(f.getFlowsForTestFlowId());
        return l;
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

}
