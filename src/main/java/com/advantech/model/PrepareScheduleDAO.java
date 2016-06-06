/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.PrepareSchedule;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class PrepareScheduleDAO extends BasicDAO {

    public PrepareScheduleDAO() {

    }

    private static Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_TWM3);
    }

    private List<PrepareSchedule> queryPrepareScheduleTable(String sql, Object... params) {
        return queryForBeanList(getConn(), PrepareSchedule.class, sql, params);
    }

    public List<PrepareSchedule> getPrepareSchedule(String po) {
        return queryPrepareScheduleTable(
            "SELECT * FROM prepare_Schedule WHERE DATEDIFF(DAY,GETDATE(),ondatetime) BETWEEN -30 AND 30 AND PO = ?",
            po
        );
    }

    public static void main(String arg0[]) {
    }
}
