/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.WorktimeValidator;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Cobot;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeFormulaSetting;
import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorktimeService {

    @Value("${HIBERNATE.JDBC.BATCHSIZE}")
    private Integer batchSize;

    @Autowired
    private WorktimeDAO worktimeDAO;

    @Autowired
    private WorktimeFormulaSettingDAO worktimeFormulaSettingDAO;

    @Autowired
    private WorktimeUploadMesService uploadMesService;

    @Autowired
    private WorktimeValidator validator;

    public List<Worktime> findAll() {
        return worktimeDAO.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        trimSearchString(info);
        List<Worktime> l = worktimeDAO.findAll(info);
        l.forEach(w -> Hibernate.initialize(w.getCobots()));
        return l;
    }

    public Worktime findByPrimaryKey(Object obj_id) {
        return worktimeDAO.findByPrimaryKey(obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return worktimeDAO.findByPrimaryKeys(ids);
    }

    public Worktime findByModel(String modelName) {
        return worktimeDAO.findByModel(modelName);
    }

    public Set<Cobot> findCobots(int obj_id) {
        Worktime w = this.findByPrimaryKey(obj_id);
        Set result = w.getCobots();
        Hibernate.initialize(result);
        return result;
    }

    public List<Worktime> findWithFullRelation(PageInfo info) {
        trimSearchString(info);
        List<Worktime> result = worktimeDAO.findWithFullRelation(info);
        result.forEach(w -> Hibernate.initialize(w.getCobots()));
        return result;
    }

    private void trimSearchString(PageInfo info) {
        if (info.getSearchString() != null && !"".equals(info.getSearchString())) {
            info.setSearchString(info.getSearchString().trim());
        }
    }

    public int insert(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            worktimeDAO.insert(w);
            uploadMesService.insert(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int insert(Worktime worktime) throws Exception {
        return this.insert(newArrayList(worktime));
    }

    //For batch modify.
    public int insertWithFormulaSetting(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 0;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            WorktimeFormulaSetting setting = w.getWorktimeFormulaSettings().get(0);
            w.setWorktimeFormulaSettings(null);
            worktimeDAO.insert(w);
            setting.setWorktime(w);
            worktimeFormulaSettingDAO.insert(setting);
            uploadMesService.insert(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    //For jqgrid edit(single row CRUD)
    public int insertWithFormulaSetting(Worktime worktime) throws Exception {
        return this.insertWithFormulaSetting(newArrayList(worktime));
    }

    public int insertSeries(String baseModelName, List<String> seriesModelNames) throws Exception {
        //Insert worktime then insert worktimeFormulaSetting & cobots setting
        
        Worktime baseW = this.findByModel(baseModelName);
        checkArgument(baseW != null, "Can't find modelName: " + baseModelName);
        List<Worktime> l = new ArrayList();

        for (String seriesModelName : seriesModelNames) {
            Worktime cloneW = (Worktime) BeanUtils.cloneBean(baseW);
            cloneW.setId(0); //CloneW is a new row, reset id.
            cloneW.setModelName(seriesModelName);
            cloneW.setReasonCode(null); //Don't copy exist reason in base model
            
            //Remove relation from FK models
            cloneW.setWorktimeFormulaSettings(null);
            cloneW.setBwFields(null);
            cloneW.setCobots(null);
            
            l.add(cloneW);
        }

        validator.checkModelNameExists(l);
        this.insert(l);

        //Insert worktimeFormulaSetting
        WorktimeFormulaSetting baseWSetting = baseW.getWorktimeFormulaSettings().get(0);
        checkState(baseWSetting != null, "Can't find formulaSetting on: " + baseModelName);
        for (Worktime w : l) {
            WorktimeFormulaSetting cloneSetting = (WorktimeFormulaSetting) BeanUtils.cloneBean(baseWSetting);
            cloneSetting.setWorktime(w);
            worktimeFormulaSettingDAO.insert(cloneSetting);
        }

        //Insert cobots setting
        Set<Cobot> cobots = baseW.getCobots();
        Set<Cobot> cloneCobotsSetting = new HashSet<>(); 
        for(Cobot c : cobots){
            Cobot cloneCobot = (Cobot) BeanUtils.cloneBean(c);
            cloneCobotsSetting.add(cloneCobot);
        }
        
        l.forEach(w ->{
            w.setCobots(cloneCobotsSetting);
            worktimeDAO.update(w);
        });
 
        return 1;
    }

    public int update(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            worktimeDAO.update(w);
            worktimeFormulaSettingDAO.update(w.getWorktimeFormulaSettings().get(0));
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int update(Worktime worktime) throws Exception {
        return this.update(newArrayList(worktime));
    }

    public int merge(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            worktimeFormulaSettingDAO.update(w.getWorktimeFormulaSettings().get(0));
            worktimeDAO.merge(w);
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int merge(Worktime worktime) throws Exception {
        return this.merge(newArrayList(worktime));
    }

    public int insertByExcel(List<Worktime> l) throws Exception {
        l.forEach(w -> {
            w.setWorktimeFormulaSettings(newArrayList(new WorktimeFormulaSetting()));
        });
        this.insertWithFormulaSetting(l);
        return 1;
    }

    public int mergeByExcel(List<Worktime> l) throws Exception {
        retriveFormulaSetting(l);

        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            //Don't need to update formula, but still need to re-calculate the formula field
            this.initUnfilledFormulaColumn(w);

            worktimeDAO.merge(w);
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;

    }

    private void retriveFormulaSetting(List<Worktime> l) {
        //Retrive settings because excel doesn't have formula setting field.
        List<WorktimeFormulaSetting> settings = worktimeFormulaSettingDAO.findWithWorktime();
        Map<Integer, WorktimeFormulaSetting> settingMap = new HashMap();
        settings.forEach((setting) -> {
            settingMap.put(setting.getWorktime().getId(), setting);
        });

        l.forEach((w) -> {
            w.setWorktimeFormulaSettings(newArrayList(settingMap.get(w.getId())));
        });
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
        if (isColumnCalculated(setting.getMachineWorktime())) {
            //Set machine worktime
            w = setCobotWorktime(w);
        }
    }

    private boolean isColumnCalculated(int i) {
        return i == 1;
    }

    public Worktime setCobotWorktime(Worktime w) {
        //Find cobots setting if cobots is not provide when user use excel batch update model
        Set<Cobot> cobots = w.getCobots() == null ? this.findCobots(w.getId()) : w.getCobots();
        if (cobots != null && !cobots.isEmpty()) {
            BigDecimal machineWorktime = cobots.stream()
                    .map(x -> x.getWorktimeSeconds())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(1, RoundingMode.HALF_UP);
            w.setMachineWorktime(machineWorktime);
        }
        return w;
    }

    //For sysop batch insert data into database.
    public int saveOrUpdate(List<Worktime> l) throws Exception {
        for (int i = 0; i < l.size(); i++) {
            Worktime w = l.get(i);
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

    public int delete(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            worktimeDAO.delete(w);
            uploadMesService.delete(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int delete(Worktime w) throws Exception {
        return this.delete(newArrayList(w));
    }

    public int delete(Integer... ids) throws Exception {
        List<Worktime> worktimes = worktimeDAO.findByPrimaryKeys(ids);
        return this.delete(worktimes);
    }

    public int delete(int id) throws Exception {
        Worktime worktime = this.findByPrimaryKey(id);
        worktimeDAO.delete(worktime);
        uploadMesService.portParamInit();
        uploadMesService.delete(worktime);
        return 1;
    }

    public void reUpdateAllFormulaColumn() throws Exception {
        List<Worktime> l = this.findAll();
        this.merge(l);
    }

    private void flushIfReachFetchSize(int currentRow) {
        if (currentRow % batchSize == 0 && currentRow > 0) {
            worktimeDAO.flushSession();
        }
    }

}
