/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
import com.advantech.dao.WorktimeFormulaSettingDAO;
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
public class WorktimeFormulaSettingService extends BasicServiceImpl<Integer, WorktimeFormulaSetting> {

    @Autowired
    private WorktimeFormulaSettingDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<WorktimeFormulaSetting> findByWorktime(int worktimeId) {
        return dao.findByWorktime(worktimeId);
    }

}
