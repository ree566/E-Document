/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabPassStationErrorRecordDAO;
import com.advantech.model.BabPassStationErrorRecord;
import static com.google.common.base.Preconditions.checkState;
import java.util.List;
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
public class BabPassStationErrorRecordService {

    private static final Logger log = LoggerFactory.getLogger(BabPassStationErrorRecordService.class);

    @Autowired
    private BabPassStationErrorRecordDAO dao;

    public List<BabPassStationErrorRecord> findAll() {
        return dao.findAll();
    }

    public BabPassStationErrorRecord findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(BabPassStationErrorRecord pojo) {
        BabPassStationErrorRecord existRec = dao.findByBabPassStationRecord(pojo.getBabPassStationRecord());
        checkState(existRec == null, "Exception record is already exist");
        return dao.insert(pojo);
    }

    public int update(BabPassStationErrorRecord pojo) {
        return dao.update(pojo);
    }

    public int delete(BabPassStationErrorRecord pojo) {
        return dao.delete(pojo);
    }

}
