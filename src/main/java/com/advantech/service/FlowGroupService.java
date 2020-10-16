/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.FlowGroupDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowGroup;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FlowGroupService {

    @Autowired
    private FlowGroupDAO flowGroupDAO;

    public List<FlowGroup> findAll() {
        return flowGroupDAO.findAll();
    }

    public List<FlowGroup> findAll(PageInfo info) {
        return flowGroupDAO.findAll(info);
    }

    public FlowGroup findByPrimaryKey(Object obj_id) {
        return flowGroupDAO.findByPrimaryKey(obj_id);
    }

    public int insert(FlowGroup flowGroup) {
        return flowGroupDAO.insert(flowGroup);
    }

    public int update(FlowGroup flowGroup) {
        return flowGroupDAO.update(flowGroup);
    }

    public int delete(FlowGroup flowGroup) {
        return flowGroupDAO.delete(flowGroup);
    }

}
