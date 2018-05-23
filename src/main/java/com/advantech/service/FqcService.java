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
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.*;
import java.util.Date;
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
public class FqcService {

    @Autowired
    private FqcDAO fqcDAO;

    @Autowired
    private FqcSettingHistoryService fqcSettingHistoryService;

    @Autowired
    private FqcLoginRecordService fqcLoginRecordService;

    @Autowired
    private PassStationRecordService passStationRecordService;
    
    @Autowired
    private WebServiceRV rv;

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
        
        String jobnumber = loginRecord.getJobnumber();
        
        pojo.setBabStatus(BabStatus.CLOSED);
        this.insert(pojo);
        
        FqcSettingHistory history = new FqcSettingHistory(pojo, jobnumber);
        history.setBeginTime(pojo.getBeginTime());
        history.setLastUpdateTime(pojo.getLastUpdateTime());
        fqcSettingHistoryService.insert(history);
        
        List<PassStationRecord> records = rv.getPassStationRecords(pojo.getPo());
        records.removeIf(rec -> !jobnumber.equals(rec.getUserNo()) || 
                rec.getCreateDate().before(pojo.getBeginTime()) || 
                rec.getCreateDate().after(pojo.getLastUpdateTime()));
        passStationRecordService.insert(records);
        
        return 1;
    }

    public int stationComplete(Fqc pojo) {
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
