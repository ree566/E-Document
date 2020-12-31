/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Worktime;
import com.advantech.repo.WorktimeFormulaSettingRepository;
import com.advantech.model.WorktimeFormulaSetting;
import com.advantech.repo.WorktimeRepository;
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
public class WorktimeFormulaSettingService {

    @Autowired
    private WorktimeRepository worktimeRepo;

    @Autowired
    private WorktimeFormulaSettingRepository repo;

    public List<WorktimeFormulaSetting> findAll() {
        return repo.findAll();
    }

    public WorktimeFormulaSetting findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<WorktimeFormulaSetting> findByWorktime(int worktimeId) {
        Worktime w = worktimeRepo.getOne(worktimeId);
        return repo.findByWorktime(w);
    }

    public int insert(WorktimeFormulaSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int update(WorktimeFormulaSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int delete(WorktimeFormulaSetting pojo) {
        repo.delete(pojo);
        return 1;
    }

}
