/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.PassStationRecord;
import com.advantech.dao.PassStationRecordDAO;
import com.advantech.webservice.WebServiceRV;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class PassStationRecordService {

    @Autowired
    private PassStationRecordDAO passStationRecordDAO;

    @Autowired
    private WebServiceRV rv;

    public List<PassStationRecord> findAll() {
        return passStationRecordDAO.findAll();
    }

    public PassStationRecord findByPrimaryKey(Object obj_id) {
        return passStationRecordDAO.findByPrimaryKey(obj_id);
    }

    public List<PassStationRecord> findByPo(String po) {
        return passStationRecordDAO.findByPo(po);
    }

    public int insert(PassStationRecord pojo) {
        return passStationRecordDAO.insert(pojo);
    }

    public int insert(List<PassStationRecord> l) {
        passStationRecordDAO.insert(l);
        return 1;
    }

    public int insertFromMes(String po, String jobnumber) {
        List<PassStationRecord> history = this.findByPo(po);
        List<PassStationRecord> l = rv.getPassStationRecords(po);

        history.removeIf(f -> !jobnumber.equals(f.getUserNo()));
        l.removeIf(f2 -> !jobnumber.equals(f2.getUserNo()));

        List<PassStationRecord> newData = (List<PassStationRecord>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            this.insert(newData);
        }
        return 1;
    }

    public int update(PassStationRecord pojo) {
        return passStationRecordDAO.update(pojo);
    }

    public int delete(PassStationRecord pojo) {
        return passStationRecordDAO.delete(pojo);
    }

}
