/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.FBN;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class FBNDAO extends BasicDAO {

    public FBNDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<FBN> queryFBNTable(String sql, Object... params) {
        return queryForBeanList(getConn(), FBN.class, sql, params);
    }

    public List<FBN> getSensorDataInDay() {
        return queryFBNTable("SELECT * FROM LS_FBN_Sort");
    }

    //利用檢視表(過濾後FBN資料表資訊)得到當前sensor時間 websocket用 
    public List<Map> getSensorInstantlyStatus() {
        return queryForMapList(getConn(), "SELECT * FROM LS_GetSenRealTime");
    }

    public List<Map> getBalancePerGroup(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_balanceDetailPerGroup(?)", BABid);
    }

    public FBN getBABFinalStationSensorStatus(int BABid) {
        List l = queryProcForBeanList(getConn(), FBN.class, "{CALL LS_babFinalStationSensorStatus(?)}", BABid);
        return l.isEmpty() ? null : (FBN) l.get(0);
    }
}
