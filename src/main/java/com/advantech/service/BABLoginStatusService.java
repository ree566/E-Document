/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.BABPeopleRecord;
import com.advantech.model.BABLoginStatusDAO;
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

    public BABLoginStatus getUser(String jobnumber) {
        return babLoginStatusDAO.getUser(jobnumber);
    }

    public List<BABLoginStatus> getBABLoginStatus(int lineId) {
        return babLoginStatusDAO.getBABLoginStatus(lineId);
    }

    public BABLoginStatus getBABLoginStatus(int lineId, int station) {
        return babLoginStatusDAO.getBABLoginStatus(lineId, station);
    }

    public boolean firstStationBABLogin(int lineNo, String jobnumber) {
        return babLoginStatusDAO.firstStationBABLogin(lineNo, jobnumber);
    }

    public boolean firstStationBABLogout(int lineNo) {
        return babLoginStatusDAO.firstStationBABLogout(lineNo);
    }

    public boolean babLogin(int lineId, int station, String jobnumber) {
        return babLoginStatusDAO.babLogin(lineId, station, jobnumber);
    }

    public boolean changeUser(int lineId, int station, String jobnumber) {
        return babLoginStatusDAO.changeUser(lineId, station, jobnumber);
    }

    public boolean deleteUserFromStation(int lineId, int station) {
        return babLoginStatusDAO.deleteUserFromStation(lineId, station);
    }

    public boolean recordBABPeople(int BABid, int station, String jobnumber) {
        return babLoginStatusDAO.recordBABPeople(new BABPeopleRecord(BABid, station, jobnumber));
    }

    public BABPeopleRecord getBABPeopleRecord(int lineId, int station) {
        return babLoginStatusDAO.getBABPeopleRecord(lineId, station);
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int lineId) {
        return babLoginStatusDAO.getBABPeopleRecord(lineId);
    }
}
