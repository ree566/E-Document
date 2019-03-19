/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.WorkCenterDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.WorkCenter;
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
public class WorkCenterService {

    @Autowired
    private WorkCenterDAO workCenterDAO;

    public List<WorkCenter> findAll() {
        return workCenterDAO.findAll();
    }

    public List<WorkCenter> findAll(PageInfo info) {
        return workCenterDAO.findAll(info);
    }

    public WorkCenter findByPrimaryKey(Object obj_id) {
        return workCenterDAO.findByPrimaryKey(obj_id);
    }

    public int insert(WorkCenter pojo) {
        return workCenterDAO.insert(pojo);
    }

    public int update(WorkCenter pojo) {
        return workCenterDAO.update(pojo);
    }

    public int delete(WorkCenter pojo) {
        return workCenterDAO.delete(pojo);
    }

    public int delete(int obj_id) {
        WorkCenter w = this.findByPrimaryKey(obj_id);
        return workCenterDAO.delete(w);
    }

}
