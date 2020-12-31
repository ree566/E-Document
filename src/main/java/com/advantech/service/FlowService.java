/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.FlowRepository;
import com.advantech.repo.FlowGroupRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FlowService {

    @Autowired
    private FlowRepository repo;

    @Autowired
    private FlowGroupRepository flowGroupRepo;

    public List<Flow> findAll() {
        return repo.findAll();
    }

    public List<Flow> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public Flow findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public Flow findByFlowName(String flowName) {
        return repo.findByName(flowName);
    }

    public List<Flow> findByFlowGroup(int flowGroupId) {
        FlowGroup fg = flowGroupRepo.getOne(flowGroupId);
        return repo.findByFlowGroup(fg);
    }

    public List<Flow> findByParent(Integer parent_id) {
        List l = new ArrayList();
        Flow f = repo.getOne(parent_id);
        l.addAll(f.getFlowsForTestFlowId());
        return l;
    }

    public List<Flow> findFlowWithSub() {
        List<Flow> l = this.findAll();
        l.forEach((f) -> {
            Hibernate.initialize(f.getFlowsForTestFlowId());
        });
        return l;
    }

    public int insert(Flow flow) {
        repo.save(flow);
        return 1;
    }

    public int update(Flow flow) {
        repo.save(flow);
        return 1;
    }

    public int delete(int id) {
        Flow flow = this.findByPrimaryKey(id);
        repo.delete(flow);
        return 1;
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
