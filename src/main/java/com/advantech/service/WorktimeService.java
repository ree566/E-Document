/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import java.math.BigDecimal;
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
public class WorktimeService {

    @Autowired
    private WorktimeDAO worktimeDAO;

    public List<Worktime> findAll() {
        return (List<Worktime>) worktimeDAO.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        return worktimeDAO.findAll(info);
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
        initUnfilledFormulaColumn(worktime);
        return worktimeDAO.insert(worktime);
    }

    public int update(Worktime worktime) {
        initUnfilledFormulaColumn(worktime);
        return worktimeDAO.update(worktime);
    }

    public int update(List<Worktime> l) {
        for (Worktime w : l) {
            worktimeDAO.update(w);
        }
        return 1;
    }

    public int merge(Worktime worktime) {
        initUnfilledFormulaColumn(worktime);
        return worktimeDAO.merge(worktime);
    }

    public void initUnfilledFormulaColumn(Worktime w) {
        if (isDecimalColumnNeedReset(w.getCleanPanelAndAssembly())) {
            w.setDefaultCleanPanelAndAssembly();
        }
        if (isDecimalColumnNeedReset(w.getProductionWt())) {
            w.setDefaultProductWt();
        }
        if (isDecimalColumnNeedReset(w.getSetupTime())) {
            w.setDefaultSetupTime();
        }
        if (isDecimalColumnNeedReset(w.getAssyToT1())) {
            w.setDefaultAssyToT1();
        }
        if (isDecimalColumnNeedReset(w.getT2ToPacking())) {
            w.setDefaultT2ToPacking();
        }
        if (w.getAssyStation() == null || w.getAssyStation() == 0) {
            w.setDefaultAssyStation();
        }
        if (w.getPackingStation() == null || w.getPackingStation() == 0) {
            w.setDefaultPackingStation();
        }
        if (isDecimalColumnNeedReset(w.getAssyKanbanTime())) {
            w.setDefaultAssyKanbanTime();
        }
        if (isDecimalColumnNeedReset(w.getPackingKanbanTime())) {
            w.setDefaultPackingKanbanTime();
        }
    }

    private boolean isDecimalColumnNeedReset(BigDecimal d) {
        return d == null || d.compareTo(BigDecimal.ZERO) == 0;
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

    public int delete(int id) {
        Worktime worktime = this.findByPrimaryKey(id);
        return worktimeDAO.delete(worktime);
    }

}
