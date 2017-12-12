/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.PassStation;
import com.advantech.dao.PassStationDAO;
import com.advantech.webservice.WebServiceRV;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class PassStationService {

    @Autowired
    private PassStationDAO passStationDAO;

    public List<PassStation> getPassStation() {
        return passStationDAO.getPassStation();
    }

    public List<PassStation> getPassStation(String PO, int apsLineId, String type) {
        return passStationDAO.getPassStation(PO, apsLineId, type);
    }

    public boolean insertPassStation(List<PassStation> l) {
        return passStationDAO.insertPassStation(l);
    }

    public List<Map> getCellLastGroupStatusView() {
        return passStationDAO.getCellLastGroupStatusView();
    }

    public void checkDifferenceAndInsert(String PO, String type, Integer apsLineId) {

        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, type);
        Iterator it = l.iterator();
        while (it.hasNext()) {
            PassStation p = (PassStation) it.next();
            if (!Objects.equals(p.getLineId(), apsLineId)) {
                it.remove();
            } else {
                p.setType(type);
            }
        }

        List<PassStation> history = this.getPassStation(PO, apsLineId, type);
        List<PassStation> newData = (List<PassStation>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            this.insertPassStation(newData);
        }
    }

    public List<Map> getAllCellPerPcsHistory(String PO, String type, Integer lineName, Integer minPcs, Integer maxPcs, String startDate, String endDate) {
        if (minPcs != null && maxPcs != null && minPcs > maxPcs) {
            maxPcs = maxPcs + minPcs;
            minPcs = maxPcs - minPcs;
            maxPcs = maxPcs - minPcs;
            //http://javarevisited.blogspot.com/2013/02/swap-two-numbers-without-third-temp-variable-java-program-example-tutorial.html#ixzz4SDLwO600
        }
        return passStationDAO.getAllCellPerPcsHistory(PO, type, lineName, minPcs, maxPcs, startDate, endDate);
    }
}
