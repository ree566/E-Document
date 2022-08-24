/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
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
public class WorkCenterService extends BasicServiceImpl<Integer, WorkCenter>{

    @Autowired
    private WorkCenterDAO dao;
    
    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<WorkCenter> findAll(PageInfo info) {
        return dao.findAll(info);
    }
   
    public List<WorkCenter> findByBusinessGroup(int businessGroupId) {
        return dao.findByBusinessGroup(businessGroupId);
    }    

    public int delete(int obj_id) {
        WorkCenter w = this.findByPrimaryKey(obj_id);
        return dao.delete(w);
    }

}
