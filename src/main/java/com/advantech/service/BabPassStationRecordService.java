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
import static com.google.common.base.Preconditions.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.math.NumberUtils;
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
    private WebServiceRV rv;

    @Autowired
    private BabSettingHistoryService babSettingService;

    public List<BabPassStationRecord> findAll() {
        return dao.findAll();
    }

    public BabPassStationRecord findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public BabPassStationRecord findLastProcessingByTagName(String tagName) {
        return dao.findLastProcessingByTagName(tagName);
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

        //Check input bab and barcode is correct or not
        List<BabSettingHistory> settings = babSettingService.findByBab(bab);
        BabSettingHistory currentTag = settings.stream()
                .filter(p -> tagName.equals(p.getTagName().getName())).findFirst()
                .orElse(null);

        checkState(currentTag != null, "Can't find processing record");

        Bab userInput = bab;
        Bab b = currentTag.getBab();
        checkState(userInput.getId() == b.getId(), "Processing record id not match");

        checkBarcodeSn(b, barcode);

        /*
            Check barcode rule for hole line is correct or not 
            Current barcode can't exist in current tagName multiple times
            Same tagName and different bab -> Can't exist more than 1 time
            Same tagName and same bab -> Can't input more than 3 times
         */
        List<BabPassStationRecord> barcodeRecords = dao.findByBarcodeAndTagName(barcode, tagName);
        List<BabPassStationRecord> diffBabRecords = barcodeRecords.stream().filter(p -> p.getBab().getId() != bab.getId()).collect(toList());
        List<BabPassStationRecord> sameBabRecords = barcodeRecords.stream().filter(p -> p.getBab().getId() == bab.getId()).collect(toList());

        checkState(diffBabRecords.isEmpty(), "Barcode input too much times(Exist in other bab record)");

        List<BabPassStationRecord> barcodeInCurrentTagName = sameBabRecords.stream()
                .filter(p -> tagName.equals(p.getTagName().getName()) && barcode.equals(p.getBarcode()))
                .collect(toList());
        int inputCount = barcodeInCurrentTagName.size() + 1;
        checkState(inputCount <= 3, "Barcode(" + barcode + ") input too much times");

        //Check barcode after first station
        if (currentTag.getStation() > 1) {
            BabSettingHistory prevTag = settings.stream()
                    .filter(p -> p.getStation() == currentTag.getStation() - 1).findFirst()
                    .orElse(null);
            checkPreviousStation(sameBabRecords, prevTag, barcode);
        }

        int barcodeInput = (int) sameBabRecords.stream()
                .filter(p -> tagName.equals(p.getTagName().getName()))
                .count();

        //Check when current barcode first input
        if (barcodeInput > 0 && barcodeInCurrentTagName.isEmpty()) {
            checkPreviousBarcode(barcodeRecords, currentTag, barcode);
        } else if (!barcodeInCurrentTagName.isEmpty()) {
//            checkBarcodeIsAfterLastInput(barcodeRecords, currentTag, barcode);
        }

        BabPassStationRecord rec = new BabPassStationRecord();
        rec.setBab(currentTag.getBab());
        rec.setTagName(currentTag.getTagName());
        rec.setBarcode(barcode);
        this.insert(rec);

        return inputCount;
    }

    private void checkPreviousStation(List<BabPassStationRecord> barcodeRecords, BabSettingHistory tag, String barcode) {
        List<BabPassStationRecord> prevBarcodeRecords = barcodeRecords.stream()
                .filter(p -> Objects.equals(p.getTagName(), tag.getTagName()) && barcode.equals(p.getBarcode()))
                .collect(toList());
        checkState(prevBarcodeRecords.size() > 1, "Barcode(" + barcode + ") is not finished yet on " + tag.getTagName().getName());
    }

    private void checkPreviousBarcode(List<BabPassStationRecord> barcodeRecords, BabSettingHistory tag, String barcode) {

        String prevBarcode = getLastInputBarcode(barcodeRecords, tag);

        List<BabPassStationRecord> prevBarcodes = barcodeRecords.stream()
                .filter(p -> Objects.equals(tag.getTagName(), p.getTagName()) && prevBarcode.equals(p.getBarcode()))
                .collect(toList());

        checkState(prevBarcodes.size() > 1, "Barcode(" + prevBarcode + ") is not finished yet");
    }

    private void checkBarcodeIsAfterLastInput(List<BabPassStationRecord> barcodeRecords, BabSettingHistory tag, String barcode) {
        String prevBarcode = getLastInputBarcode(barcodeRecords, tag);

        int currentNum = getBarcodeSerialNumber(barcode);
        int prevNum = getBarcodeSerialNumber(prevBarcode);

        checkState(currentNum >= prevNum, "Barcode number can't less than last input");
    }

    private String getLastInputBarcode(List<BabPassStationRecord> barcodeRecords, BabSettingHistory tag) {
        List<BabPassStationRecord> barcodeInCurrentTagName = barcodeRecords.stream()
                .filter(p -> Objects.equals(tag.getTagName(), p.getTagName()))
                .sorted(Comparator.comparing(BabPassStationRecord::getBarcode))
                .collect(toList());

        BabPassStationRecord lastInput = barcodeInCurrentTagName.get(barcodeInCurrentTagName.size() - 1);
        return lastInput.getBarcode();
    }

    private void checkBarcodeSn(Bab b, String barcode) {
        String po = rv.getPoByBarcode(barcode, Factory.DEFAULT);
        checkState(Objects.equals(po, b.getPo()), "Barcode's SN not match");
    }

    private int getBarcodeSerialNumber(String barcode) {
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(barcode);
        String st = null;
        while (m.find()) {
            st = m.group();
        }
        return NumberUtils.createInteger(st);
    }

    public int update(BabPassStationRecord pojo) {
        return dao.update(pojo);
    }

    public int delete(BabPassStationRecord pojo) {
        return dao.delete(pojo);
    }

}
