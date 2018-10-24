/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabPassStationRecordDAO;
import com.advantech.model.Bab;
import com.advantech.model.BabPassStationRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.checkState;
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
public class BabPassStationRecordService {

    @Autowired
    private BabPassStationRecordDAO dao;

    @Autowired
    private BabSettingHistoryService settingService;
    
    @Autowired
    private WebServiceRV rv;

    public List<BabPassStationRecord> findAll() {
        return dao.findAll();
    }

    public BabPassStationRecord findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(BabPassStationRecord pojo) {
        return dao.insert(pojo);
    }

    public int checkStationInfoAndInsert(Bab bab, String tagName, String barcode) {
        BabSettingHistory setting = settingService.findProcessingByTagName(tagName);
        checkState(setting != null, "Can't find processing record");
        
        Bab userInput = bab;
        Bab b = setting.getBab();
        checkState(userInput.getId() == b.getId(), "Processing record id not match");
        
        String modelName = rv.getModelNameByBarcode(barcode, Factory.DEFAULT);
        checkState(Objects.equals(modelName, b.getModelName()), "Barcode's modelName not match");
        
        BabPassStationRecord rec = new BabPassStationRecord();
        rec.setBab(setting.getBab());
        rec.setTagName(setting.getTagName());
        rec.setBarcode(barcode);
        this.insert(rec);
        
        return 1;
    }

    public int update(BabPassStationRecord pojo) {
        return dao.update(pojo);
    }

    public int delete(BabPassStationRecord pojo) {
        return dao.delete(pojo);
    }

}
