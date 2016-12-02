/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.PassStation;
import com.advantech.model.PassStationDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class PassStationService {

    private final PassStationDAO passStationDAO;

    protected PassStationService() {
        passStationDAO = new PassStationDAO();
    }

    public List<PassStation> getPassStation() {
        return passStationDAO.getPassStation();
    }
    
    public List<PassStation> getPassStation(String PO) {
        return passStationDAO.getPassStation(PO);
    }

    public List<PassStation> getPassStationToday(String PO) {
        return passStationDAO.getPassStationToday(PO);
    }

    public boolean insertPassStation(List<PassStation> l) {
        return passStationDAO.insertPassStation(l);
    }
}
