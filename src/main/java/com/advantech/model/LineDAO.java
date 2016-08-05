/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Line;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class LineDAO extends BasicDAO {

    private final int LINE_OPEN_SIGN = 1;
    private final int LINE_CLOSE_SIGN = 0;

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private boolean updateLine(String sql, Object... params) {
        return update(getConn(), sql, params);
    }

    private List<Line> queryLineTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Line.class, sql, params);
    }

    public List<Line> getLine() {
        return queryLineTable("SELECT * FROM LS_Line");
    }

    public Line getLine(int lineNO) {
        List l = queryLineTable("SELECT * FROM LS_Line WHERE id = ?", lineNO);
        return !l.isEmpty() ? (Line) l.get(0) : null;
    }

    public List<Line> getLine(String sitefloor) {
        return queryLineTable("SELECT * FROM LS_Line where sitefloor = ?", sitefloor);
    }

    public boolean openSingleLine(int lineNo) {
        return updateLineStatus(LINE_OPEN_SIGN, lineNo);
    }

    // End the line.(Station 1)
    public boolean closeSingleLine(int lineNo) {
        return updateLineStatus(LINE_CLOSE_SIGN, lineNo);
    }

    private boolean updateLineStatus(int openOrClose, int lineNo) {
        return updateLine("UPDATE LS_Line SET isused = ? WHERE id = ?", openOrClose, lineNo);
    }

    public boolean allLineEnd() {
        return updateLine("UPDATE LS_Line SET isused = 0");
    }

}
