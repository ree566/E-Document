/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.BabSettingHistory;
import com.advantech.dao.BabSettingHistoryDAO;
import com.advantech.model.Bab;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class BabSettingHistoryService {

    @Autowired
    private BabSettingHistoryDAO babSettingHistoryDAO;

    public List<BabSettingHistory> findAll() {
        return babSettingHistoryDAO.findAll();
    }

    public BabSettingHistory findByPrimaryKey(Object obj_id) {
        return babSettingHistoryDAO.findByPrimaryKey(obj_id);
    }

    public List<BabSettingHistory> findByBab(Bab b) {
        return babSettingHistoryDAO.findByBab(b);
    }

    public BabSettingHistory findByBabAndStation(Bab b, int station) {
        return babSettingHistoryDAO.findByBabAndStation(b, station);
    }

    public int insert(BabSettingHistory pojo) {
        return babSettingHistoryDAO.insert(pojo);
    }

    public int update(BabSettingHistory pojo) {
        return babSettingHistoryDAO.update(pojo);
    }

    public int delete(BabSettingHistory pojo) {
        return babSettingHistoryDAO.delete(pojo);
    }

//    public boolean recordBABPeople(int BABid, int station, String jobnumber) {
//        BabPeopleRecord bRecord = this.getLastBABPeopleRecord(BABid, station);
//        if (bRecord == null || !bRecord.getUser_id().equals(jobnumber)) {
//            List l = new ArrayList();
//            l.add(new BabPeopleRecord(BABid, station, jobnumber));
//            return babLoginStatusDAO.recordBABPeople(l);
//        } else {
//            return true; //If the user is already exist, don't do anything.
//        }
//    }
}
