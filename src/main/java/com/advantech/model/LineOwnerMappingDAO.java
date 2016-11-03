/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class LineOwnerMappingDAO extends BasicDAO {

    public LineOwnerMappingDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    public List<Map> getLineOwnerMappingView() {
        return queryForMapList(this.getConn(), "SELECT * FROM lineOwnerMappingView");
    }

    public List<Map> getResponsorPerSitefloorView() {
        return queryForMapList(this.getConn(), "SELECT * FROM responsorPerSitefloorView");
    }

}
