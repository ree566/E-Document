/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeAutouploadSettingDAO;
import com.advantech.model.WorktimeAutouploadSetting;
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
public class WorktimeAutouploadSettingService {

    @Autowired
    private WorktimeAutouploadSettingDAO worktimeAutouploadSettingDAO;

    public List<WorktimeAutouploadSetting> findAll() {
        return worktimeAutouploadSettingDAO.findAll();
    }

    public WorktimeAutouploadSetting findByPrimaryKey(Object obj_id) {
        return worktimeAutouploadSettingDAO.findByPrimaryKey(obj_id);
    }

    public List<WorktimeAutouploadSetting> findByPrimaryKeys(Integer... id) {
        return worktimeAutouploadSettingDAO.findByPrimaryKeys(id);
    }

    public int insert(WorktimeAutouploadSetting pojo) {
        return worktimeAutouploadSettingDAO.insert(pojo);
    }

    public int update(WorktimeAutouploadSetting pojo) {
        return worktimeAutouploadSettingDAO.update(pojo);
    }

    public int delete(WorktimeAutouploadSetting pojo) {
        return worktimeAutouploadSettingDAO.delete(pojo);
    }

}
