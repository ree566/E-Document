/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.PrepareSchedule;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class PrepareScheduleDAO extends BasicDAO {

    public PrepareScheduleDAO() {

    }

    private static Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<PrepareSchedule> queryPrepareScheduleTable(String sql, Object... params) {
        return queryForBeanList(getConn(), PrepareSchedule.class, sql, params);
    }

    //只取正負30天的工單
    public List<PrepareSchedule> getPrepareSchedule(String po) {
        return queryPrepareScheduleTable("SELECT * FROM prepareScheduleView WHERE PO = ?", po);
    }

    public List<Map> getTestStandardTime(String modelName) {
        return queryForMapList(getConn(), "{CALL exp_getNschedule(?)}", modelName);
    }

    public static void main(String arg0[]) {
    }
}
