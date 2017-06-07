/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeFormulaSetting;
import static com.google.common.collect.Lists.newArrayList;
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

    @Autowired
    private WorktimeFormulaSettingDAO worktimeFormulaSettingDAO;

    public List<Worktime> findAll() {
        return worktimeDAO.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        return worktimeDAO.findAll(info);
    }

    public Worktime findByPrimaryKey(Object obj_id) {
        Worktime w = worktimeDAO.findByPrimaryKey(obj_id);
        Hibernate.initialize(w.getWorktimeFormulaSettings());
        return w;
    }

    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return worktimeDAO.findByPrimaryKeys(ids);
    }

    public Worktime findByModel(String modelName) {
        return worktimeDAO.findByModel(modelName);
    }

    public int insert(Worktime worktime) {
        worktimeDAO.insert(worktime);
        return 1;
    }

    public int insertWithFormulaSetting(Worktime worktime) {
        initUnfilledFormulaColumn(worktime);
        WorktimeFormulaSetting setting = worktime.getWorktimeFormulaSettings().get(0);
        worktime.setWorktimeFormulaSettings(null);
        this.insert(worktime);
        setting.setWorktime(worktime);
        worktimeFormulaSettingDAO.insert(setting);
        return 1;
    }

    public int update(Worktime worktime) {
        initUnfilledFormulaColumn(worktime);
        worktimeDAO.update(worktime);
        return 1;
    }

    public int update(List<Worktime> l) {
        for (Worktime w : l) {
            worktimeDAO.update(w);
        }
        return 1;
    }

    public int merge(Worktime worktime) {
        initUnfilledFormulaColumn(worktime);

        //Merge formula setting first
        List<WorktimeFormulaSetting> existSettings = worktimeFormulaSettingDAO.findByWorktime(worktime.getId());
        WorktimeFormulaSetting setting = worktime.getWorktimeFormulaSettings().get(0);
        setting.setWorktime(worktime);
        if (existSettings.isEmpty()) {
            worktimeFormulaSettingDAO.insert(setting);
        } else {
            setting.setId(existSettings.get(0).getId());
            worktimeFormulaSettingDAO.merge(setting);
        }

        //Add the persisted WorktimeFormulaSetting object
        worktime.setWorktimeFormulaSettings(newArrayList(setting));
        worktimeDAO.merge(worktime);
        return 1;
    }

    public void initUnfilledFormulaColumn(Worktime w) {
        //Lazy loading
        WorktimeFormulaSetting setting = w.getWorktimeFormulaSettings().get(0);

        if (isColumnCalculated(setting.getCleanPanelAndAssembly())) {
            w.setDefaultCleanPanelAndAssembly();
        }
        if (isColumnCalculated(setting.getProductionWt())) {
            w.setDefaultProductWt();
        }
        if (isColumnCalculated(setting.getSetupTime())) {
            w.setDefaultSetupTime();
        }
        if (isColumnCalculated(setting.getAssyToT1())) {
            w.setDefaultAssyToT1();
        }
        if (isColumnCalculated(setting.getT2ToPacking())) {
            w.setDefaultT2ToPacking();
        }
        if (isColumnCalculated(setting.getAssyStation())) {
            w.setDefaultAssyStation();
        }
        if (isColumnCalculated(setting.getPackingStation())) {
            w.setDefaultPackingStation();
        }
        if (isColumnCalculated(setting.getAssyKanbanTime())) {
            w.setDefaultAssyKanbanTime();
        }
        if (isColumnCalculated(setting.getPackingKanbanTime())) {
            w.setDefaultPackingKanbanTime();
        }
    }

    private boolean isColumnCalculated(int i) {
        return i == 1;
    }

    public int saveOrUpdate(List<Worktime> l) {
        for (int i = 0; i < l.size(); i++) {
            Worktime w = l.get(i);
            System.out.println("insert row: " + i + " \\Model: " + w.getModelName());
            Worktime existW = this.findByModel(w.getModelName());
            if (existW == null) {
                this.insertWithFormulaSetting(w);
            } else {
                w.setId(existW.getId());
                this.merge(w);
            }
        }
        return 1;
    }

    public int delete(int id) {
        Worktime worktime = this.findByPrimaryKey(id);
        return worktimeDAO.delete(worktime);
    }

}
