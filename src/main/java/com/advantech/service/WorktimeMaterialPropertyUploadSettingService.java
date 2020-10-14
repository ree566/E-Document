/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeMaterialPropertyUploadSettingDAO;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
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
public class WorktimeMaterialPropertyUploadSettingService {

    @Autowired
    private WorktimeMaterialPropertyUploadSettingDAO worktimeAutouploadSettingDAO;

    public List<WorktimeMaterialPropertyUploadSetting> findAll() {
        return worktimeAutouploadSettingDAO.findAll();
    }

    public WorktimeMaterialPropertyUploadSetting findByPrimaryKey(Object obj_id) {
        return worktimeAutouploadSettingDAO.findByPrimaryKey(obj_id);
    }

    public int insert(WorktimeMaterialPropertyUploadSetting pojo) {
        return worktimeAutouploadSettingDAO.insert(pojo);
    }

    public int update(WorktimeMaterialPropertyUploadSetting pojo) {
        return worktimeAutouploadSettingDAO.update(pojo);
    }

    public int delete(WorktimeMaterialPropertyUploadSetting pojo) {
        return worktimeAutouploadSettingDAO.delete(pojo);
    }

}
