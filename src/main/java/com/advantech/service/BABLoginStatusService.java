/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.BABPeopleRecord;
import com.advantech.model.BABLoginStatusDAO;
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
public class BABLoginStatusService {

    @Autowired
    private BABLoginStatusDAO babLoginStatusDAO;

    public List<BABLoginStatus> getBABLoginStatus() {
        return babLoginStatusDAO.getBABLoginStatus();
    }

    public List<BABLoginStatus> getBABLoginStatus(String jobnumber) {
        return babLoginStatusDAO.getBABLoginStatus(jobnumber);
    }

    public List<BABLoginStatus> getBABLoginStatus(int lineId, int station) {
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
        BABPeopleRecord bRecord = this.getLastBABPeopleRecord(BABid, station);
        if (bRecord == null || !bRecord.getUser_id().equals(jobnumber)) {
            List l = new ArrayList();
            l.add(new BABPeopleRecord(BABid, station, jobnumber));
            return babLoginStatusDAO.recordBABPeople(l);
        } else {
            return true; //If the user is already exist, don't do anything.
        }
    }

    public BABPeopleRecord getLastBABPeopleRecord(int BABid, int station) {
        List<BABPeopleRecord> l = this.getBABPeopleRecord(BABid, station);
        return !l.isEmpty() ? l.get(l.size() - 1) : null;
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int BABid, int station) {
        return babLoginStatusDAO.getBABPeopleRecord(BABid, station);
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int BABid) {
        return babLoginStatusDAO.getBABPeopleRecord(BABid);
    }
}
