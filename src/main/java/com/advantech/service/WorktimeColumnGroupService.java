/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
import com.advantech.dao.WorktimeColumnGroupDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.WorktimeColumnGroup;
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
public class WorktimeColumnGroupService extends BasicServiceImpl<Integer, WorktimeColumnGroup> {

    @Autowired
    private WorktimeColumnGroupDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<WorktimeColumnGroup> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public WorktimeColumnGroup findByUnit(int obj_id) {
        return dao.findByUnit(obj_id);
    }

    public int delete(int id) {
        WorktimeColumnGroup w = this.findByPrimaryKey(id);
        return dao.delete(w);
    }

}
