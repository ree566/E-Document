/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.WorktimeMaterialPropertyUploadSettingRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeMaterialPropertyUploadSettingService {

    @Autowired
    private WorktimeMaterialPropertyUploadSettingRepository repo;

    public List<WorktimeMaterialPropertyUploadSetting> findAll() {
        return repo.findAll();
    }

    public WorktimeMaterialPropertyUploadSetting findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(WorktimeMaterialPropertyUploadSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int update(WorktimeMaterialPropertyUploadSetting pojo) {
        repo.save(pojo);
        return 1;
    }

    public int delete(WorktimeMaterialPropertyUploadSetting pojo) {
        repo.delete(pojo);
        return 1;
    }

}
