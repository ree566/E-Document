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
import com.advantech.model.FqcModelStandardTime;
import com.advantech.model.FqcProducitvityHistory;
import com.advantech.model.FqcSettingHistory;
import com.advantech.model.PassStationRecord;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.*;
import java.util.Comparator;
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
    private FqcProducitvityHistoryService fqcProducitvityHistoryService;

    @Autowired
    private FqcModelStandardTimeService fqcModelStandardTimeService;

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

    public List<Fqc> findReconnectable(int fqcLine_id, String po) {
        return fqcDAO.findReconnectable(fqcLine_id, po);
    }

    /*
        Check user is login and po is exist or not.
     */
    public int checkAndInsert(Fqc pojo) {
        FqcLine fqcLine = pojo.getFqcLine();
        FqcLoginRecord loginRecord = fqcLoginRecordService.findByFqcLine(fqcLine.getId());
        checkArgument(loginRecord != null, "Can't find login record in this line");
        this.insert(pojo);
        String jobnumber = loginRecord.getJobnumber();
        FqcSettingHistory history = new FqcSettingHistory(pojo, jobnumber);
        history.setBeginTime(pojo.getBeginTime());
        fqcSettingHistoryService.insert(history);

        return 1;
    }

    public int reconnectAbnormal(Fqc fqc) {
        fqc.setBabStatus(null);
        fqc.setLastUpdateTime(null);
        fqcDAO.update(fqc);
        FqcSettingHistory history = fqcSettingHistoryService.findByFqc(fqc);
        history.setLastUpdateTime(null);
        fqcSettingHistoryService.update(history);

        FqcProducitvityHistory pHistory = fqcProducitvityHistoryService.findByFqc(fqc.getId());
        fqcProducitvityHistoryService.delete(pHistory);
        return 1;
    }

    public int stationComplete(Fqc pojo, int timeCost) {

        Date now = new Date();
        pojo.setBabStatus(BabStatus.CLOSED);
        pojo.setLastUpdateTime(now);

        FqcSettingHistory history = fqcSettingHistoryService.findByFqc(pojo);
        history.setLastUpdateTime(now);
        fqcSettingHistoryService.update(history);

        List<PassStationRecord> records = rv.getPassStationRecords(pojo.getPo());
        records.removeIf(rec -> !history.getJobnumber().equals(rec.getUserNo())
                || rec.getCreateDate().before(pojo.getBeginTime())
                || rec.getCreateDate().after(pojo.getLastUpdateTime()));
        passStationRecordService.insert(records);

        String modelName = pojo.getModelName();

        FqcModelStandardTime standardTime = null;
        List<FqcModelStandardTime> l = fqcModelStandardTimeService.findAll();

        if (!l.isEmpty()) {
            standardTime = l.stream()
                    .filter(f -> modelName.contains(f.getModelNameCategory()))
                    .max(Comparator.comparing(s -> s.getModelNameCategory().length()))
                    .orElse(null);
        }

        this.update(pojo);
        fqcProducitvityHistoryService.insert(new FqcProducitvityHistory(pojo, records.size(),
                timeCost, standardTime == null ? 0 : standardTime.getStandardTime()));
        return 1;
    }

    public int addAbnormalReconnectableSignAndClose(Fqc pojo, int timeCost) {
        this.stationComplete(pojo, timeCost);
        pojo.setBabStatus(BabStatus.UNFINSHED_RECONNECTABLE);
        this.update(pojo);
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
