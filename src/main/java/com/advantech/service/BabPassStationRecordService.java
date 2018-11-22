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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(BabPassStationRecordService.class);

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

    /**
     * @param bab
     * @param tagName
     * @param barcode
     * @return Number of the barcode input count in database
     */
    public int checkStationInfoAndInsert(Bab bab, String tagName, String barcode) {
        List<BabPassStationRecord> barcodeRecords = dao.findByBabAndBarcode(bab, tagName, barcode);
        int inputCount = barcodeRecords.size() + 1;
        checkState(inputCount <= 3, "Barcode(" + barcode + ") input too much times");

        BabSettingHistory setting = settingService.findProcessingByTagName(tagName);
        checkState(setting != null, "Can't find processing record");

        Bab userInput = bab;
        Bab b = setting.getBab();
        checkState(userInput.getId() == b.getId(), "Processing record id not match");

        String po = rv.getPoByBarcode(barcode, Factory.DEFAULT);
        boolean poCheckFlag = Objects.equals(po, b.getPo());
        checkState(poCheckFlag, "Barcode's SN not match");

        BabPassStationRecord rec = new BabPassStationRecord();
        rec.setBab(setting.getBab());
        rec.setTagName(setting.getTagName());
        rec.setBarcode(barcode);
        this.insert(rec);

        return inputCount;
    }

    public int update(BabPassStationRecord pojo) {
        return dao.update(pojo);
    }

    public int delete(BabPassStationRecord pojo) {
        return dao.delete(pojo);
    }

}
