/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.PassStation;
import static com.advantech.model.BasicDAO.getDBUtilConn;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class PassStationDAO extends BasicDAO {

    public PassStationDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(BasicDAO.SQL.Way_Chien_WebAccess);
    }

    public List<PassStation> getPassStation() {
        return queryForBeanList(this.getConn(), PassStation.class, "SELECT * FROM machineThrough");
    }
    
    public List<PassStation> getPassStation(String PO) {
        return queryForBeanList(this.getConn(), PassStation.class, "SELECT * FROM machineThrough WHERE PO = ?", PO);
    }
    
    public List<Map> getCellPerPcsHistory(String PO, int lineId) {
        return queryProcForMapList(this.getConn(), "{CALL cellDiffPerPcs(?,?)}", PO, lineId);
    }
    
    public List<Map> getCellLastGroupStatusView(){
        return queryForMapList(this.getConn(), "SELECT * FROM cellLastGroupStatusView");
    }

    public boolean insertPassStation(List<PassStation> l) {
        return update(
                getConn(),
                "INSERT INTO machineThrough(barcode, PO, lineName, lineId, station, createDate, userNo, userName) VALUES(?,?,?,?,?,?,?,?)",
                l,
                "barcode", "PO", "lineName", "lineId", "station", "createDate", "userNo", "userName"
        );
    }
}
