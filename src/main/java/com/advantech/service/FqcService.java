/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FqcDAO;
import com.advantech.model.BabStatus;
import com.advantech.model.Fqc;
import com.advantech.model.FqcLine;
import com.advantech.model.FqcLoginRecord;
import com.advantech.model.FqcSettingHistory;
import com.advantech.model.PassStationRecord;
import static com.google.common.base.Preconditions.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FqcService {

    @Autowired
    private FqcDAO fqcDAO;

    @Autowired
    private FqcSettingHistoryService fqcSettingHistoryService;

    @Autowired
    private FqcLoginRecordService fqcLoginRecordService;

    @Autowired
    private PassStationRecordService passStationRecordService;

    public List<Fqc> findAll() {
        return fqcDAO.findAll();
    }

    public Fqc findByPrimaryKey(Object obj_id) {
        return fqcDAO.findByPrimaryKey(obj_id);
    }

    public List<Fqc> findProcessing() {
        return fqcDAO.findProcessing();
    }

    public List<Fqc> findProcessing(int fqcLine_id) {
        return fqcDAO.findProcessing(fqcLine_id);
    }

    /*
        Check user is login and po is exist or not.
     */
    public int checkAndInsert(Fqc pojo) {
        FqcLine fqcLine = pojo.getFqcLine();
        FqcLoginRecord loginRecord = fqcLoginRecordService.findByFqcLine(fqcLine.getId());
        checkArgument(loginRecord != null, "Can't find login record in this line");
        List<Fqc> processing = this.findProcessing(fqcLine.getId());
        Fqc samePoRecord = processing.stream().filter(p -> Objects.equals(p.getPo(), pojo.getPo())).findFirst().orElse(null);
        checkArgument(samePoRecord == null, "Same po is already exist");
        this.insert(pojo);
        fqcSettingHistoryService.insert(new FqcSettingHistory(pojo, loginRecord.getJobnumber()));
        return 1;
    }

    public int stationComplete(Fqc pojo) {
        Date now = new Date();
        FqcSettingHistory history = fqcSettingHistoryService.findByFqc(pojo);
        
        passStationRecordService.insertFromMes(pojo.getPo(), history.getJobnumber());

        List<PassStationRecord> records = passStationRecordService.findByPo(pojo.getPo());
        records.removeIf(rec -> !history.getJobnumber().equals(rec.getUserNo()));

        pojo.setLastUpdateTime(now);
        history.setLastUpdateTime(now);

        pojo.setBabStatus(records.isEmpty() ? BabStatus.NO_RECORD : BabStatus.CLOSED);

        this.update(pojo);
        fqcSettingHistoryService.update(history);

        return 1;
    }

    public int insert(Fqc pojo) {
        return fqcDAO.insert(pojo);
    }

    public int update(Fqc pojo) {
        return fqcDAO.update(pojo);
    }

    public int delete(Fqc pojo) {
        return fqcDAO.delete(pojo);
    }

}
