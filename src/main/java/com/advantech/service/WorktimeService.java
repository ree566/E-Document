/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeFormulaSetting;
import static com.google.common.collect.Lists.newArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final int fetchSize = 20;

    @Autowired
    private WorktimeDAO worktimeDAO;

    @Autowired
    private WorktimeFormulaSettingDAO worktimeFormulaSettingDAO;
    
    @Autowired
    private WorktimeUploadMesService uploadMesService;

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

    public List<Worktime> findWithFullRelation(PageInfo info) {
        return worktimeDAO.findWithFullRelation(info);
    }

    public int insert(List<Worktime> l) throws Exception {
        int i = 1;
        for (Worktime w : l) {
            this.insert(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int insert(Worktime worktime) throws Exception {
        worktimeDAO.insert(worktime);
        uploadMesService.uploadToMes(worktime);
        return 1;
    }

    //For batch modify.
    public int insertWithFormulaSetting(List<Worktime> l) throws Exception {
        int i = 0;
        for (Worktime w : l) {
            this.insertWithFormulaSetting(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    //For jqgrid edit(single row CRUD)
    public int insertWithFormulaSetting(Worktime worktime) throws Exception {
        initUnfilledFormulaColumn(worktime);
        WorktimeFormulaSetting setting = worktime.getWorktimeFormulaSettings().get(0);
        worktime.setWorktimeFormulaSettings(null);
        this.insert(worktime);
        setting.setWorktime(worktime);
        worktimeFormulaSettingDAO.insert(setting);
        uploadMesService.uploadToMes(worktime);
        return 1;
    }

    public int update(List<Worktime> l) throws Exception {
        int i = 1;
        for (Worktime w : l) {
            this.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int update(Worktime worktime) throws Exception {
        initUnfilledFormulaColumn(worktime);
        worktimeFormulaSettingDAO.update(worktime.getWorktimeFormulaSettings().get(0));
        worktimeDAO.update(worktime);
        uploadMesService.uploadToMesWithCheckRevision(worktime);
        return 1;
    }

    public int merge(List<Worktime> l) throws Exception {
        int i = 1;
        for (Worktime w : l) {
            this.merge(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int merge(Worktime worktime) throws Exception {
        initUnfilledFormulaColumn(worktime);
        worktimeFormulaSettingDAO.update(worktime.getWorktimeFormulaSettings().get(0));
        worktimeDAO.merge(worktime);
        uploadMesService.uploadToMesWithCheckRevision(worktime);
        return 1;
    }

    public int insertByExcel(List<Worktime> l) throws Exception {
        int i = 1;
        for (Worktime w : l) {
            w.setWorktimeFormulaSettings(newArrayList(new WorktimeFormulaSetting()));
            this.insertWithFormulaSetting(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int mergeByExcel(List<Worktime> l) throws Exception {
        List<WorktimeFormulaSetting> settings = worktimeFormulaSettingDAO.findWithWorktime();
        Map<Integer, WorktimeFormulaSetting> settingMap = new HashMap();
        for (WorktimeFormulaSetting setting : settings) {
            settingMap.put(setting.getWorktime().getId(), setting);
        }

        int i = 1;
        for (Worktime w : l) {
            w.setWorktimeFormulaSettings(newArrayList(settingMap.get(w.getId())));
            initUnfilledFormulaColumn(w);
            worktimeDAO.merge(w);
            flushIfReachFetchSize(i++);
        }
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

    public int saveOrUpdate(List<Worktime> l) throws Exception {
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

    public int delete(Integer... ids) {
        List<Worktime> worktimes = worktimeDAO.findByPrimaryKeys(ids);
        int i = 1;
        for (Worktime w : worktimes) {
            worktimeDAO.delete(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int delete(int id) throws Exception {
        Worktime worktime = this.findByPrimaryKey(id);
        worktimeDAO.delete(worktime);
        worktime.setAssyPackingSop("");
        worktime.setTestSop("");
        uploadMesService.uploadToMes(worktime);
        return 1;
    }

    public void reUpdateAllFormulaColumn() throws Exception {
        List<Worktime> l = this.findAll();
        this.merge(l);
    }

    private void flushIfReachFetchSize(int currentRow) {
        if (currentRow % fetchSize == 0 && currentRow > 0) {
            worktimeDAO.flushSession();
        }
    }

    public void checkModelExists(Worktime worktime) throws Exception {
        Worktime existW = worktimeDAO.findByModel(worktime.getModelName());
        boolean checkFlag;
        if (worktime.getId() == 0) {
            checkFlag = existW != null;
        } else {
            checkFlag = existW != null && existW.getId() != worktime.getId();
        }
        if (checkFlag == true) {
            throw new Exception("This modelName &lt;" + worktime.getModelName() + "&gt; is already exist.");
        }
    }

    public void checkModelExists(List<Worktime> worktimes) throws Exception {
        Map<String, Integer> modelMap = new HashMap();
        List<Worktime> allWorktime = this.findAll();
        for (Worktime w : allWorktime) {
            modelMap.put(w.getModelName(), w.getId());
        }

        for (Worktime w : worktimes) {
            boolean checkFlag;
            Integer worktimeId = modelMap.get(w.getModelName());
            if (w.getId() == 0) {
                checkFlag = worktimeId != null;
            } else {
                checkFlag = worktimeId != null && worktimeId != w.getId();
            }
            if (checkFlag == true) {
                throw new Exception("This modelName &lt;" + w.getModelName() + "&gt; is already exist.");
            }
        }
    }

}
