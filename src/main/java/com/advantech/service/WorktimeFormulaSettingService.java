/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeFormulaSettingDAO;
import com.advantech.model.WorktimeFormulaSetting;
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
    private WorktimeFormulaSettingDAO worktimeFormulaSettingDAO;

    public List<WorktimeFormulaSetting> findAll() {
        return worktimeFormulaSettingDAO.findAll();
    }

    public WorktimeFormulaSetting findByPrimaryKey(Object obj_id) {
        return worktimeFormulaSettingDAO.findByPrimaryKey(obj_id);
    }

    public List<WorktimeFormulaSetting> findByWorktime(int worktimeId) {
        return worktimeFormulaSettingDAO.findByWorktime(worktimeId);
    }

    public int insert(WorktimeFormulaSetting pojo) {
        return worktimeFormulaSettingDAO.insert(pojo);
    }

    public int update(WorktimeFormulaSetting pojo) {
        return worktimeFormulaSettingDAO.update(pojo);
    }

    public int delete(WorktimeFormulaSetting pojo) {
        return worktimeFormulaSettingDAO.delete(pojo);
    }

}
