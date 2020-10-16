/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeMaterialPropertyUploadSettingDAO extends AbstractDao<Integer, WorktimeMaterialPropertyUploadSetting> implements BasicDAO<WorktimeMaterialPropertyUploadSetting> {

    @Override
    public List<WorktimeMaterialPropertyUploadSetting> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public WorktimeMaterialPropertyUploadSetting findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(WorktimeMaterialPropertyUploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(WorktimeMaterialPropertyUploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(WorktimeMaterialPropertyUploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
