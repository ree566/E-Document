/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Fbn;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FbnDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<Fbn> queryFBNTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Fbn.class, sql, params);
    }

    public List<Fbn> getSensorDataInDay() {
        return queryFBNTable("SELECT * FROM LS_FBN_Sort");
    }

    public Fbn getLastInputData() {
        List l = queryFBNTable("SELECT TOP 1 * FROM LS_FBN_Sort ORDER BY ID DESC");
        return !l.isEmpty() ? (Fbn) l.get(0) : null;
    }

    //利用檢視表(過濾後FBN資料表資訊)得到當前sensor時間 websocket用 
    public List<Map> getSensorCurrentStatus() {
        return queryForMapList(getConn(), "SELECT * FROM LS_GetSenRealTime");
    }

    public List<Fbn> getSensorStatus(int BABid) {
        return queryFBNTable("{CALL sp_sensorStatus(?)}", BABid);
    }

    public Fbn getBABFinalStationSensorStatus(int BABid) {
        List l = queryProcForBeanList(getConn(), Fbn.class, "{CALL LS_babFinalStationSensorStatus(?)}", BABid);
        return l.isEmpty() ? null : (Fbn) l.get(0);
    }

    public List<Map> getTotalAbnormalData(int BABid) {
        return queryProcForMapList(getConn(), "{CALL sensorTotalAbnormalCheck(?)}", BABid);
    }

    public List<Map> getAbnormalData(int BABid) {
        return queryProcForMapList(getConn(), "{CALL sensorAbnormalCheck(?)}", BABid);
    }

    public boolean sensorDataClean(String date) {
        return updateProc(this.getConn(), "{CALL sensorDataCleanProc(?)}", date);
    }
}
