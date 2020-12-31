/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeColumnGroupRepository;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Unit;
import com.advantech.model.WorktimeColumnGroup;
import com.advantech.repo.UnitRepository;
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
    private WorktimeColumnGroupRepository repo;
    
    @Autowired
    private UnitRepository unitRepo;

    public List<WorktimeColumnGroup> findAll() {
        return repo.findAll();
    }

    public List<WorktimeColumnGroup> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public WorktimeColumnGroup findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public WorktimeColumnGroup findByUnit(int obj_id) {
        Unit u = unitRepo.getOne(obj_id);
        return repo.findByUnit(u);
    }

    public int insert(WorktimeColumnGroup w) {
        repo.save(w);
        return 1;
    }

    public int update(WorktimeColumnGroup w) {
        repo.save(w);
        return 1;
    }

    public int delete(int id) {
        WorktimeColumnGroup w = this.findByPrimaryKey(id);
        repo.delete(w);
        return 1;
    }

}
