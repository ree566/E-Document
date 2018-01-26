/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 * Procedure object can't be normally generate by hibernate 
 * Result map order disrupted by hibernate
 */
@Repository
public class SystemReportDao extends BasicDAO {

    private Connection getConn() {
        return super.getDBUtilConn(SQL.WebAccess);
    }

    //Bab各工單回復狀況以及詳細
    public List<Map> getCountermeasureForExcel(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL usp_Excel_Countermeasure(?,?)}", startDate, endDate);
    }

    //Bab各站別詳細
    public List<Map> getPersonalAlmForExcel(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL usp_Excel_PersonalAlarm(?,?)}", startDate, endDate);
    }

    //For效率報表
    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL countermeasureAndPersonalAlmDownExcel(?,?)}", startDate, endDate);
    }
    
    //沒有儲存紀錄的工單
    public List<Map> getEmptyRecordDownExcel(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL usp_Excel_EmptyRecord(?,?)}", startDate, endDate);
    }
    
}
