/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import static com.advantech.model.BasicDAO.getDBUtilConn;
import static com.advantech.model.BasicDAO.queryForMapList;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class WorkTimeDAO {

    private static Connection getConn() {
        return getDBUtilConn(BasicDAO.SQL.Way_Chien_WebAccess);
    }
    //抓取測試工時

    public List<Map> getTestStandardTime(String modelName) {
        return queryForMapList(getConn(), "SELECT * FROM workTimePerModelView WHERE Model_name = ?", modelName);
    }
}
