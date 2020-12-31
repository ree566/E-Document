/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.WorktimeRepository;
import com.advantech.repo.WorktimeFormulaSettingRepository;
import com.advantech.helper.WorktimeValidator;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeFormulaSetting;
import com.advantech.webservice.port.StandardWorkReasonQueryPort;
import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
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
    private WorktimeRepository repo;

    @Autowired
    private WorktimeFormulaSettingRepository worktimeFormulaSettingRepo;

    @Autowired
    private WorktimeUploadMesService uploadMesService;

    @Autowired
    private WorktimeValidator validator;

    public List<Worktime> findAll() {
        return repo.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public Worktime findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return repo.findAllById(Arrays.asList(ids));
    }

    public Worktime findByModel(String modelName) {
        return repo.findByModelName(modelName);
    }

    public List<Worktime> findWithFullRelation(PageInfo info) {
//        return repo.findWithFullRelation(info);
        return this.findAll(info);
    }

    public List<SheetView> findSheetView() {
        return repo.findSheetView();
    }

    public int insert(List<Worktime> l) throws Exception {
        repo.saveAll(l);
        uploadMesService.portParamInit();
        for (Worktime w : l) {
            uploadMesService.insert(w);
        }
        return 1;
    }

    public int insert(Worktime worktime) throws Exception {
        return this.insert(newArrayList(worktime));
    }

    /*
        For batch modify.
        Formula info is provided from worktime insert page 
        Need to generate new formula object when excel import because there's no formula info from excel
     */
    public int insertWithFormulaSetting(List<Worktime> l) throws Exception {
        for (Worktime w : l) {
            WorktimeFormulaSetting setting = w.getWorktimeFormulaSettings().get(0);
            worktimeFormulaSettingRepo.save(setting);

            initUnfilledFormulaColumn(w);
            w.setWorktimeFormulaSettings(newArrayList(setting));
            repo.save(w);
            uploadMesService.insert(w);
        }
        return 1;
    }

    //For jqgrid edit(single row CRUD)
    public int insertWithFormulaSetting(Worktime worktime) throws Exception {
        return this.insertWithFormulaSetting(newArrayList(worktime));
    }

    public int insertSeries(String baseModelName, List<String> seriesModelNames) throws Exception {
        Worktime baseW = this.findByModel(baseModelName);
        checkArgument(baseW != null, "Can't find modelName: " + baseModelName);
        List<Worktime> l = new ArrayList();

        for (String seriesModelName : seriesModelNames) {
            Worktime cloneW = (Worktime) BeanUtils.cloneBean(baseW);
            cloneW.setId(0); //CloneW is a new row, reset id.
            cloneW.setModelName(seriesModelName);
            cloneW.setWorktimeFormulaSettings(null);
            cloneW.setBwFields(null);
            cloneW.setReasonCode(null); //Don't copy exist reason in base model
            l.add(cloneW);
        }

        validator.checkModelNameExists(l);
        this.insert(l);

        WorktimeFormulaSetting baseWSetting = baseW.getWorktimeFormulaSettings().get(0);
        checkState(baseWSetting != null, "Can't find formulaSetting on: " + baseModelName);
        for (Worktime w : l) {
            WorktimeFormulaSetting cloneSetting = (WorktimeFormulaSetting) BeanUtils.cloneBean(baseWSetting);
            cloneSetting.setWorktime(w);
            worktimeFormulaSettingRepo.save(cloneSetting);
        }

        return 1;
    }

    public int update(List<Worktime> l) throws Exception {
        uploadMesService.portParamInit();
        for (Worktime w : l) {
            initUnfilledFormulaColumn(w);
            worktimeFormulaSettingRepo.save(w.getWorktimeFormulaSettings().get(0));
            repo.save(w);
            uploadMesService.update(w);
        }
        return 1;
    }

    public int update(Worktime worktime) throws Exception {
        return this.update(newArrayList(worktime));
    }

    public int merge(List<Worktime> l) throws Exception {
        return this.update(l);
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
        for (Worktime w : l) {
            //Don't need to update formula, but still need to re-calculate the formula field
            this.initUnfilledFormulaColumn(w);
            repo.save(w);
            uploadMesService.update(w);
        }
        return 1;

    }

    private void retriveFormulaSetting(List<Worktime> l) {
        //Retrive settings because excel doesn't have formula setting field.
        List<WorktimeFormulaSetting> settings = worktimeFormulaSettingRepo.findAll();
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
    }

    private boolean isColumnCalculated(int i) {
        return i == 1;
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
        repo.deleteAll(l);
        uploadMesService.portParamInit();
        for (Worktime w : l) {
            uploadMesService.delete(w);
        }
        return 1;
    }

    public int delete(Worktime w) throws Exception {
        return this.delete(newArrayList(w));
    }

    public int delete(Integer... ids) throws Exception {
        List<Worktime> worktimes = this.findByPrimaryKeys(ids);
        repo.deleteAll(worktimes);
        uploadMesService.portParamInit();
        for (Worktime w : worktimes) {
            uploadMesService.delete(w);
        }
        return 1;
    }

    public void reUpdateAllFormulaColumn() throws Exception {
        List<Worktime> l = this.findAll();
        this.merge(l);
    }

}
