/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SqlViewDAO;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.Bab;
import com.advantech.model.view.BabAvg;
import com.advantech.model.view.BabLastBarcodeStatus;
import com.advantech.model.view.BabLastGroupStatus;
import com.advantech.model.view.SensorCurrentGroupStatus;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SqlViewService {

    @Autowired
    private SqlViewDAO sqlViewDAO;

    @Autowired
    private PropertiesReader reader;

    public List<BabAvg> findBabAvg(int bab_id) {
        switch (reader.getBabDataCollectMode()) {
            case AUTO:
                return sqlViewDAO.findBabAvg(bab_id);
            case MANUAL:
                return sqlViewDAO.findBabAvgWithBarcode(bab_id);
            default:
                return new ArrayList();
        }
    }

    public List<BabAvg> findBabAvgInHistory(int bab_id) {
        return sqlViewDAO.findBabAvgInHistory(bab_id);
    }

    public List<Worktime> findWorktime() {
        return sqlViewDAO.findWorktime();
    }

    public Worktime findWorktime(String modelName) {
        return sqlViewDAO.findWorktime(modelName);
    }

    public UserInfoRemote findUserInfoRemote(String jobnumber) {
        return sqlViewDAO.findUserInfoRemote(jobnumber);
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(int bab_id) {
        return sqlViewDAO.findBabLastGroupStatus(bab_id);
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(List<Bab> babs) {
        List<BabLastGroupStatus> l = new ArrayList();
        babs.forEach((b) -> {
            l.addAll(sqlViewDAO.findBabLastGroupStatus(b.getId()));
        });
        return l;
    }

    public List<BabLastBarcodeStatus> findBabLastBarcodeStatus(List<Bab> babs) {
        List<BabLastBarcodeStatus> l = new ArrayList();
        babs.forEach((b) -> {
            l.addAll(sqlViewDAO.findBabLastBarcodeStatus(b.getId()));
        });
        return l;
    }

    public List<SensorCurrentGroupStatus> findSensorCurrentGroupStatus(int bab_id) {
        return sqlViewDAO.findSensorCurrentGroupStatus(bab_id);
    }

    public List<Map> findSensorStatus(int bab_id) {
        return sqlViewDAO.findSensorStatus(bab_id);
    }

    public List<Map> findBarcodeStatus(int bab_id) {
        return sqlViewDAO.findBarcodeStatus(bab_id);
    }

    public List<Map> findBalanceDetail(int bab_id) {
        return sqlViewDAO.findBalanceDetail(bab_id);
    }

    public List<Map> findBalanceDetailWithBarcode(int bab_id) {
        return sqlViewDAO.findBalanceDetailWithBarcode(bab_id);
    }

    public List<Map> findBabDetail(int lineType_id, int floor_id, DateTime sD, DateTime eD, boolean isAboveStandard) {
        switch (reader.getBabDataCollectMode()) {
            case AUTO:
                return sqlViewDAO.findBabDetail(lineType_id, floor_id, sD, eD, isAboveStandard);
            case MANUAL:
                return sqlViewDAO.findBabDetailWithBarcode(lineType_id, floor_id, sD, eD, isAboveStandard);
            default:
                return new ArrayList();
        }
    }

    public List<Map> findLineBalanceCompareByBab(int bab_id) {
        switch (reader.getBabDataCollectMode()) {
            case AUTO:
                return sqlViewDAO.findLineBalanceCompareByBab(bab_id);
            case MANUAL:
                return sqlViewDAO.findLineBalanceCompareByBabWithBarcode(bab_id);
            default:
                return new ArrayList();
        }
    }

    public List<Map> findLineBalanceCompare(String modelName, String lineTypeName) {
        return sqlViewDAO.findLineBalanceCompare(modelName, lineTypeName);
    }

    public List<Map> findSensorStatusPerStationToday() {
        return sqlViewDAO.findSensorStatusPerStationToday();
    }

    public List<Map> findBabPcsDetail(String modelName, String lineType, DateTime startDate, DateTime endDate) {
        switch (reader.getBabDataCollectMode()) {
            case AUTO:
                return sqlViewDAO.findBabPcsDetail(modelName, lineType, startDate, endDate);
            case MANUAL:
                return sqlViewDAO.findBabPcsDetailWithBarcode(modelName, lineType, startDate, endDate);
            default:
                return new ArrayList();
        }
    }

    public List<Map> findBabLineProductivity(String po, String modelName, int line_id, String jobnumber, Integer minPcs, DateTime sD, DateTime eD) {
        switch (reader.getBabDataCollectMode()) {
            case AUTO:
                return sqlViewDAO.findBabLineProductivity(po, modelName, line_id, jobnumber, minPcs, sD, eD);
            case MANUAL:
                return sqlViewDAO.findBabLineProductivityWithBarcode(po, modelName, line_id, jobnumber, minPcs, sD, eD);
            default:
                return new ArrayList();
        }
    }

    public List<Map> findBabPassStationRecord(String po, String modelName, DateTime sD, DateTime eD, String lineType) {
        return sqlViewDAO.findBabPassStationRecord(po, modelName, sD, eD, lineType);
    }

}
