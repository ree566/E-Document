/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.FBNDAO;
import com.advantech.entity.FBN;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class FBNService {

    private final FBNDAO fbnDAO;

    protected FBNService() {
        fbnDAO = new FBNDAO();
    }

    public List<FBN> getSensorDataInDay() {
        return fbnDAO.getSensorDataInDay();
    }

    public List<Map> getSensorInstantlyStatus() {
        return fbnDAO.getSensorInstantlyStatus();
    }

    public List<Map> getBalancePerGroup(int BABid) {
        return fbnDAO.getBalancePerGroup(BABid);
    }

    public FBN getBABFinalStationSensorStatus(int BABid) {
        return fbnDAO.getBABFinalStationSensorStatus(BABid);
    }

}
