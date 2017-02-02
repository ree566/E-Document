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
 * 工單帶機種功能已經轉為向WebService下request, 後續可刪除此class
 */
public class PrepareScheduleDAO extends BasicDAO {

    private static Connection getConn() {
        return getDBUtilConn(SQL.TWM3);
    }

    private List<PrepareSchedule> queryPrepareScheduleTable(String sql, Object... params) {
        return queryForBeanList(getConn(), PrepareSchedule.class, sql, params);
    }

    //只取正負30天的工單
    public List<PrepareSchedule> getPrepareSchedule(String po) {
        return queryPrepareScheduleTable("{CALL exp_getPschedule(?)}", po);
    }

}
