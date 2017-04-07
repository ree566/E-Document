/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.WorktimeColumnGroupDAO;
import com.advantech.model.Floor;
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
public class WorktimeColumnGroupService {

    @Autowired
    private WorktimeColumnGroupDAO worktimeColumnGroupDAO;

    public List<WorktimeColumnGroup> findAll() {
        return (List<WorktimeColumnGroup>) worktimeColumnGroupDAO.findAll();
    }

    public WorktimeColumnGroup findByPrimaryKey(Object obj_id) {
        return (WorktimeColumnGroup) worktimeColumnGroupDAO.findByPrimaryKey(obj_id);
    }

    public WorktimeColumnGroup findByUserType(Object obj_id) {
        return (WorktimeColumnGroup)worktimeColumnGroupDAO.findByUserType(obj_id);
    }

    public int insert(WorktimeColumnGroup w) {
        return worktimeColumnGroupDAO.insert(w);
    }

    public int update(WorktimeColumnGroup w) {
        return worktimeColumnGroupDAO.update(w);
    }

    public int delete(WorktimeColumnGroup w) {
        return worktimeColumnGroupDAO.delete(w);
    }

}
