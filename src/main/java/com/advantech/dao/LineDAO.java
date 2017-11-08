/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Line;
import com.advantech.model.LineStatus;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class LineDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private boolean updateLine(String sql, Object... params) {
        return update(getConn(), sql, params);
    }

    private List<Line> queryLineTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Line.class, sql, params);
    }

    public List<Line> getLineForJSTL() {
        return getLine();
    }

    public List<Line> getLine() {
        return queryLineTable("SELECT * FROM LS_Line");
    }

    public List<Line> getOpenedLine() {
        return queryLineTable("SELECT * FROM LS_Line WHERE lock = 0");
    }

    public Line getLine(int lineNO) {
        List l = queryLineTable("SELECT * FROM LS_Line WHERE id = ?", lineNO);
        return !l.isEmpty() ? (Line) l.get(0) : null;
    }

    public List<Line> getLine(String sitefloor) {
        if (sitefloor.length() > 3) {
            return new ArrayList();
        }
        return queryLineTable("SELECT * FROM LS_Line WHERE sitefloor = ? ORDER BY name", sitefloor);
    }

    public boolean openSingleLine(int lineNo) {
        return updateLineStatus(LineStatus.OPEN.getState(), lineNo);
    }

    // End the line.(Station 1)
    public boolean closeSingleLine(int lineNo) {
        return updateLineStatus(LineStatus.CLOSE.getState(), lineNo);
    }

    private boolean updateLineStatus(int openOrClose, int lineNo) {
        return updateLine("UPDATE LS_Line SET isused = ? WHERE id = ?", openOrClose, lineNo);
    }

    public boolean allLineEnd() {
        return updateLine("UPDATE LS_Line SET isused = ?", LineStatus.CLOSE.getState());
    }

}
