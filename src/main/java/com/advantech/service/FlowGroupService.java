/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowGroup;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.FlowGroupRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FlowGroupService {

    @Autowired
    private FlowGroupRepository repo;

    public List<FlowGroup> findAll() {
        return repo.findAll();
    }

    public List<FlowGroup> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public FlowGroup findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(FlowGroup flowGroup) {
        repo.save(flowGroup);
        return 1;
    }

    public int update(FlowGroup flowGroup) {
        repo.save(flowGroup);
        return 1;
    }

    public int delete(FlowGroup flowGroup) {
        repo.delete(flowGroup);
        return 1;
    }

}
