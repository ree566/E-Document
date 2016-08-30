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

/**
 *
 * @author Wei.Cheng
 */
public class BABLoginStatusService {

    private final BABLoginStatusDAO babLoginStatusDAO;

    public BABLoginStatusService() {
        babLoginStatusDAO = new BABLoginStatusDAO();
    }

    public List<BABLoginStatus> getBABLoginStatus() {
        return babLoginStatusDAO.getBABLoginStatus();
    }

    public boolean babLogin(int BABid, int station, String jobnumber) {
        return babLoginStatusDAO.babLogin(BABid, station, jobnumber);
    }

    public boolean changeUser(int BABid, int station, String jobnumber) {
       return babLoginStatusDAO.changeUser(BABid, station, jobnumber);
    }

    public boolean deleteUserFromStation(int BABid, int station) {
        return babLoginStatusDAO.deleteUserFromStation(BABid, station);
    }

    public boolean recordBABPeople(int BABid, int station, String jobnumber) {
        List l = new ArrayList();
        l.add(new BABPeopleRecord(BABid, station, jobnumber));
        return babLoginStatusDAO.recordBABPeople(l);
    }

    public BABPeopleRecord getExistUserInBAB(int BABid, int station) {
        return babLoginStatusDAO.getExistUserInBAB(BABid, station);
    }

    public List<BABPeopleRecord> getExistUserInBAB(int BABid) {
        return babLoginStatusDAO.getExistUserInBAB(BABid);
    }
}
