/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng Procedure object can't be normally generate by hibernate
 * Result map order disrupted by hibernate
 */
@Repository
public class SystemReportDao extends BasicDAO {

    private Connection getConn() {
        return super.getDBUtilConn(SQL.WebAccess);
    }

    //Bab各工單回復狀況以及詳細
    public List<Map> getCountermeasureForExcel(String lineTypeName, int floorId,
            String startDate, String endDate, boolean isAboveStandard) {
        return queryProcForMapList(getConn(),
                "{CALL usp_Excel_Countermeasure_1(?, ?, ?, ?, ?)}",
                lineTypeName, floorId, startDate, endDate, isAboveStandard);
    }

    //Bab各站別詳細
    public List<Map> getPersonalAlmForExcel(String lineTypeName, int floorId, String startDate,
            String endDate, boolean isAboveStandard) {
        return queryProcForMapList(getConn(),
                "{CALL usp_Excel_PersonalAlarm_1(?, ?, ?, ?, ?)}",
                lineTypeName, floorId, startDate, endDate, isAboveStandard);
    }

    //For效率報表
    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL usp_Excel_CountermeasureAndPersonalAlarm(?,?)}",
                startDate, endDate);
    }

    //沒有儲存紀錄的工單
    public List<Map> getEmptyRecordDownExcel(String lineTypeName, int floorId,
            String startDate, String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL usp_Excel_EmptyRecord_1(?, ?, ?, ?)}",
                lineTypeName, floorId, startDate, endDate);
    }

}
