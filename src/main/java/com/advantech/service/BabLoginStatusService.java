/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.BabLoginStatus;
import com.advantech.model.BabPeopleRecord;
import com.advantech.dao.BabLoginStatusDAO;
import java.util.ArrayList;
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
public class BabLoginStatusService {

    @Autowired
    private BabLoginStatusDAO babLoginStatusDAO;

    public List<BabLoginStatus> getBABLoginStatus() {
        return babLoginStatusDAO.getBABLoginStatus();
    }

    public List<BabLoginStatus> getBABLoginStatus(String jobnumber) {
        return babLoginStatusDAO.getBABLoginStatus(jobnumber);
    }

    public List<BabLoginStatus> getBABLoginStatus(int lineId, int station) {
        return babLoginStatusDAO.getBABLoginStatus(lineId, station);
    }

    public boolean insert(int BABid, int station, String jobnumber) {
        return babLoginStatusDAO.insert(BABid, station, jobnumber);
    }

    public boolean update(int BABid, int station, String jobnumber) {
        return babLoginStatusDAO.update(BABid, station, jobnumber);
    }

    public boolean delete(int BABid, int station) {
        return babLoginStatusDAO.delete(BABid, station);
    }

    public boolean recordBABPeople(int BABid, int station, String jobnumber) {
        BabPeopleRecord bRecord = this.getLastBABPeopleRecord(BABid, station);
        if (bRecord == null || !bRecord.getUser_id().equals(jobnumber)) {
            List l = new ArrayList();
            l.add(new BabPeopleRecord(BABid, station, jobnumber));
            return babLoginStatusDAO.recordBABPeople(l);
        } else {
            return true; //If the user is already exist, don't do anything.
        }
    }

    public BabPeopleRecord getLastBABPeopleRecord(int BABid, int station) {
        List<BabPeopleRecord> l = this.getBABPeopleRecord(BABid, station);
        return !l.isEmpty() ? l.get(l.size() - 1) : null;
    }

    public List<BabPeopleRecord> getBABPeopleRecord(int BABid, int station) {
        return babLoginStatusDAO.getBABPeopleRecord(BABid, station);
    }

    public List<BabPeopleRecord> getBABPeopleRecord(int BABid) {
        return babLoginStatusDAO.getBABPeopleRecord(BABid);
    }
}
