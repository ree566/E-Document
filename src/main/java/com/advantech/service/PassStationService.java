/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.PassStation;
import com.advantech.model.PassStationDAO;
import java.util.List;
import java.util.Map;

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

    public boolean insertPassStation(List<PassStation> l) {
        return passStationDAO.insertPassStation(l);
    }
    
    public List<Map> getCellLastGroupStatusView(){
        return passStationDAO.getCellLastGroupStatusView();
    }

    public List<Map> getCellPerPcsHistory(String PO) {
        return passStationDAO.getCellPerPcsHistory(PO);
    }

    public List<Map> getCellPerPcsHistory(String PO, Integer minPcs, Integer maxPcs) {
        List l = passStationDAO.getCellPerPcsHistory(PO);
        if (l.isEmpty()) {
            return l;
        } else {
            if (minPcs != null && maxPcs != null && minPcs > maxPcs) {
                maxPcs = maxPcs + minPcs;
                minPcs = maxPcs - minPcs;
                maxPcs = maxPcs - minPcs;
                //http://javarevisited.blogspot.com/2013/02/swap-two-numbers-without-third-temp-variable-java-program-example-tutorial.html#ixzz4SDLwO600
            }

            int listSize = l.size();
            minPcs = (minPcs == null ? null : (minPcs <= 0 ? 0 : (minPcs > listSize ? listSize : minPcs - 1)));
            maxPcs = (maxPcs == null ? null : (maxPcs <= 0 ? 0 : (maxPcs > listSize ? listSize : maxPcs)));

            if (minPcs == null && maxPcs != null) {
                return l.subList(0, maxPcs > listSize ? listSize : maxPcs);
            } else if (minPcs != null && maxPcs == null) {
                return l.subList(minPcs, listSize);
            } else {
                return l.subList(minPcs, maxPcs);
            }
        }
    }
}
