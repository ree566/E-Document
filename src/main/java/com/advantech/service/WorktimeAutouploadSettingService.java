/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeAutouploadSettingRepository;
import com.advantech.model.WorktimeAutouploadSetting;
import java.util.Arrays;
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
    private WorktimeAutouploadSettingRepository repo;

    public List<WorktimeAutouploadSetting> findAll() {
        return repo.findAll();
    }

    public WorktimeAutouploadSetting findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<WorktimeAutouploadSetting> findByPrimaryKeys(Integer... id) {
        return repo.findAllById(Arrays.asList(id));
    }

    public int insert(WorktimeAutouploadSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int update(WorktimeAutouploadSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int delete(WorktimeAutouploadSetting pojo) {
        repo.delete(pojo);
        return 1;
    }

}
