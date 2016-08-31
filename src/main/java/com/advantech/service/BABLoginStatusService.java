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

    public BABLoginStatus getBABLoginStatus(int lineId, int station) {
        return babLoginStatusDAO.getBABLoginStatus(lineId, station);
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

    public boolean recordBABPeople(int lineId, int station, String jobnumber) {
        List l = new ArrayList();
        l.add(new BABPeopleRecord(lineId, station, jobnumber));
        return babLoginStatusDAO.recordBABPeople(l);
    }

    public BABPeopleRecord getExistUserInBAB(int lineId, int station) {
        return babLoginStatusDAO.getExistUserInBAB(lineId, station);
    }

    public List<BABPeopleRecord> getExistUserInBAB(int lineId) {
        return babLoginStatusDAO.getExistUserInBAB(lineId);
    }
}
