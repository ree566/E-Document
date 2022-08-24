/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
import com.advantech.dao.CobotDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Cobot;
import com.advantech.model.Worktime;
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
public class CobotService extends BasicServiceImpl<Integer, Cobot> {

    @Autowired
    private CobotDAO dao;

    @Autowired
    private WorktimeService worktimeService;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<Cobot> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    @Override
    public int update(Cobot pojo) {  
        //Prevent update many to many relationship when pojo's column changed
        Cobot dataInDb = this.findByPrimaryKey(pojo.getId());
        dataInDb.setWorktimeMinutes(pojo.getWorktimeMinutes());
        dataInDb.setWorktimeSeconds(pojo.getWorktimeSeconds());
        dao.update(dataInDb);
        
//        Reset worktime when cobot's worktime is changed
        List<Worktime> l = worktimeService.findAll();
        for (Worktime w : l) {
            worktimeService.setCobotWorktime(w);
        }
        worktimeService.merge(l);
        return 1;
    }
}
