/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    public SheetViewDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.E_DOC);
    }
    
    public List<Map> getAll(){
        return queryForMapList(getConn(), "SELECT * FROM Sheet_Main_view");
    }
}
