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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorktimeService extends BasicServiceImpl<Integer, Worktime> {

    @Autowired
    private WorktimeDAO dao;

    @Autowired
    private WorktimeFormulaSettingDAO worktimeFormulaSettingService;

    @Autowired
    private WorktimeUploadMesService uploadMesService;

    @Autowired
    private WorktimeValidator validator;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<Worktime> findAll(PageInfo info) {
        trimSearchString(info);
        List<Worktime> l = dao.findAll(info);
        l.forEach(w -> Hibernate.initialize(w.getCobots()));
        return l;
    }

    public Worktime findByModel(String modelName) {
        return dao.findByModel(modelName);
    }

    public Set<Cobot> findCobots(int obj_id) {
        Worktime w = this.findByPrimaryKey(obj_id);
        Set result = w.getCobots();
        Hibernate.initialize(result);
        return result;
    }

    public List<Worktime> findWithFullRelation(PageInfo info) {
        trimSearchString(info);
        List<Worktime> result = dao.findWithFullRelation(info);
        result.forEach(w -> Hibernate.initialize(w.getCobots()));
        return result;
    }

    private void trimSearchString(PageInfo info) {
        if (info.getSearchString() != null && !"".equals(info.getSearchString())) {
            info.setSearchString(info.getSearchString().trim());
        }
    }

    public int insertWithMesUpload(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            dao.insert(w);
            uploadMesService.insert(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int insertWithMesUpload(Worktime worktime) throws Exception {
        return this.insertWithMesUpload(newArrayList(worktime));
    }

    //For batch modify.
    public int insertWithMesUploadAndFormulaSetting(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 0;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            WorktimeFormulaSetting setting = w.getWorktimeFormulaSettings().get(0);
            w.setWorktimeFormulaSettings(null);
            dao.insert(w);
            setting.setWorktime(w);
            worktimeFormulaSettingService.insert(setting);
            uploadMesService.insert(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    //For jqgrid edit(single row CRUD)
    public int insertWithFormulaSetting(Worktime worktime) throws Exception {
        return this.insertWithMesUploadAndFormulaSetting(newArrayList(worktime));
    }

    public int insertSeriesWithMesUpload(String baseModelName, List<String> seriesModelNames) throws Exception {
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
        this.insertWithMesUpload(l);

        //Insert worktimeFormulaSetting
        WorktimeFormulaSetting baseWSetting = baseW.getWorktimeFormulaSettings().get(0);
        checkState(baseWSetting != null, "Can't find formulaSetting on: " + baseModelName);
        for (Worktime w : l) {
            WorktimeFormulaSetting cloneSetting = (WorktimeFormulaSetting) BeanUtils.cloneBean(baseWSetting);
            cloneSetting.setWorktime(w);
            worktimeFormulaSettingService.insert(cloneSetting);
        }

        //Insert cobots setting
        Set<Cobot> cobots = baseW.getCobots();
        Set<Cobot> cloneCobotsSetting = new HashSet<>();
        for (Cobot c : cobots) {
            Cobot cloneCobot = (Cobot) BeanUtils.cloneBean(c);
            cloneCobotsSetting.add(cloneCobot);
        }

        l.forEach(w -> {
            w.setCobots(cloneCobotsSetting);
            dao.update(w);
        });

        return 1;
    }

    public int updateWithMesUpload(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            dao.update(w);
            worktimeFormulaSettingService.update(w.getWorktimeFormulaSettings().get(0));
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int updateWithMesUpload(Worktime worktime) throws Exception {
        return this.updateWithMesUpload(newArrayList(worktime));
    }

    public int mergeWithMesUpload(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            worktimeFormulaSettingService.update(w.getWorktimeFormulaSettings().get(0));
            dao.merge(w);
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int mergeWithMesUpload(Worktime worktime) throws Exception {
        return this.mergeWithMesUpload(newArrayList(worktime));
    }

    public int insertByExcel(List<Worktime> l) throws Exception {
        l.forEach(w -> {
            w.setWorktimeFormulaSettings(newArrayList(new WorktimeFormulaSetting()));
        });
        this.insertWithMesUploadAndFormulaSetting(l);
        return 1;
    }

    public int mergeByExcel(List<Worktime> l) throws Exception {
        retriveFormulaSetting(l);

        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            //Don't need to update formula, but still need to re-calculate the formula field
            this.initUnfilledFormulaColumn(w);

            dao.merge(w);
            uploadMesService.update(w);
            flushIfReachFetchSize(i++);
        }
        return 1;

    }

    private void retriveFormulaSetting(List<Worktime> l) {
        //Retrive settings because excel doesn't have formula setting field.
        List<WorktimeFormulaSetting> settings = worktimeFormulaSettingService.findWithWorktime();
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
        BigDecimal machineWorktime = BigDecimal.ZERO;
        if (cobots != null && !cobots.isEmpty()) {
            machineWorktime = cobots.stream()
                    .map(x -> x.getWorktimeMinutes())
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(1, RoundingMode.HALF_UP);
        }
        w.setMachineWorktime(machineWorktime);

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
                this.mergeWithMesUpload(w);
            }
        }
        return 1;
    }

    public int deleteWithMesUpload(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        int i = 1;
        for (Worktime w : l) {
            dao.delete(w);
            uploadMesService.delete(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    public int deleteWithMesUpload(Worktime w) throws Exception {
        return this.deleteWithMesUpload(newArrayList(w));
    }

    public int deleteWithMesUpload(Integer... ids) throws Exception {
        List<Worktime> worktimes = dao.findByPrimaryKeys(ids);
        return this.deleteWithMesUpload(worktimes);
    }

    public int deleteWithMesUpload(int id) throws Exception {
        Worktime worktime = this.findByPrimaryKey(id);
        dao.delete(worktime);
        uploadMesService.portParamInit();
        uploadMesService.delete(worktime);
        return 1;
    }

    public void reUpdateAllFormulaColumn() throws Exception {
        List<Worktime> l = this.findAll();
        this.mergeWithMesUpload(l);
    }

}
