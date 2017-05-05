/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeService {

    @Autowired
    private WorktimeDAO worktimeDAO;

    public List<Worktime> findAll() {
        return (List<Worktime>) worktimeDAO.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        List l = worktimeDAO.findAll(info);
        Hibernate.initialize(l);
        return l;
    }

    public Worktime findByPrimaryKey(Object obj_id) {
        return (Worktime) worktimeDAO.findByPrimaryKey(obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return worktimeDAO.findByPrimaryKeys(ids);
    }

    public Worktime findByModel(String modelName) {
        return worktimeDAO.findByModel(modelName);
    }

    public int insert(Worktime worktime) {
        checkAndSetFormulaColumn(worktime);
        return worktimeDAO.insert(worktime);
    }

    public int update(Worktime worktime) {
        checkAndSetFormulaColumn(worktime);
        return worktimeDAO.update(worktime);
    }

    public int update(List<Worktime> l) {
        for (Worktime w : l) {
            worktimeDAO.update(w);
        }
        return 1;
    }

    public void checkAndSetFormulaColumn(Worktime w) {
        if (w.getCleanPanelAndAssembly() == null || w.getCleanPanelAndAssembly() == 0) {
            w.setDefaultCleanPanelAndAssembly();
        }
        if (w.getProductionWt() == null || w.getProductionWt() == 0) {
            w.setDefaultProductWt();
        }
        if (w.getSetupTime() == null || w.getSetupTime() == 0) {
            w.setDefaultSetupTime();
        }
        if (w.getAssyToT1() == null || w.getAssyToT1() == 0) {
            w.setDefaultAssyToT1();
        }
        if (w.getT2ToPacking() == null || w.getT2ToPacking() == 0) {
            w.setDefaultT2ToPacking();
        }
        if (w.getAssyStation() == null || w.getAssyStation() == 0) {
            w.setDefaultAssyStation();
        }
        if (w.getPackingStation() == null || w.getPackingStation() == 0) {
            w.setDefaultPackingStation();
        }
        if (w.getAssyKanbanTime() == null || w.getAssyKanbanTime() == 0) {
            w.setDefaultAssyKanbanTime();
        }
        if (w.getPackingKanbanTime() == null || w.getPackingKanbanTime() == 0) {
            w.setDefaultPackingKanbanTime();
        }
    }

    public int saveOrUpdate(List<Worktime> l) {
        for (int i = 0; i < l.size(); i++) {
            Worktime w = l.get(i);
            System.out.println("insert row: " + i + " \\Model: " + w.getModelName());
            Worktime existW = this.findByModel(w.getModelName().trim());
            if (existW == null) {
                this.insert(w);
            } else {
                w.setId(existW.getId());
                worktimeDAO.merge(w);
            }
        }
        return 1;
    }

    public int delete(List<Worktime> l) {
        for (Worktime m : l) {
            this.delete(m);
        }
        return 1;
    }

    public int delete(Object pojo) {
        return worktimeDAO.delete(pojo);
    }

}
