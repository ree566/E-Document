/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.BAB;
import com.advantech.entity.Countermeasure;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
public class CountermeasureDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(CountermeasureDAO.class);

    public CountermeasureDAO() {
    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<Countermeasure> queryCountermeasureTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Countermeasure.class, sql, params);
    }

    public List<Countermeasure> getCountermeasures() {
        return this.queryCountermeasureTable("SELECT * FROM Countermeasure");
    }

    public Countermeasure getCountermeasure(int BABid) {
        List l = this.queryCountermeasureTable("SELECT * FROM Countermeasure WHERE BABid = ?", BABid);
        return l.isEmpty() ? null : (Countermeasure) l.get(0);
    }

    public List<Map> getCountermeasureView() {
        return queryForMapList(this.getConn(), "SELECT * FROM CountermeasureView");
    }

    public List<Map> getUnFillCountermeasureBabs() {
        return queryForMapList(this.getConn(), "SELECT * FROM unFillCountermeasureView ORDER BY btime DESC");
    }

    public List<Map> getUnFillCountermeasureBabs(String sitefloor) {
        return queryForMapList(this.getConn(), "SELECT * FROM unFillCountermeasureView WHERE sitefloor = ?", sitefloor);
    }

    public List<Map> getCountermeasureView(String lineType, String sitefloor, String startDate, String endDate) {
        return queryProcForMapList(this.getConn(), "{CALL countermeasureDownExcel(?,?,?,?)}", lineType, sitefloor, startDate, endDate);
    }

    public List<Map> getErrorCode() {
        return queryForMapList(getConn(), "SELECT * FROM errorCode");
    }

    public List<Map> getErrorCode(int cm_id) {
        return queryForMapList(getConn(), "SELECT * FROM CountermeasureErrorCodeView WHERE cm_id = ?", cm_id);
    }

    public List<Map> getActionCode() {
        return queryForMapList(getConn(), "SELECT * FROM actionCode");
    }

    public List<Map> getEditor(int cm_id) {
        return queryForMapList(getConn(), "select editor from countermeasureEditorView WHERE cm_id = ?", cm_id);
    }

    public boolean insertCountermeasure(int BABid, String solution, List<String> actionCodes, String editor) {

        boolean flag = false;
        Connection conn = null;

        try {
            QueryRunner qRunner = new QueryRunner();

            conn = this.getConn();
            conn.setAutoCommit(false);

            Object[] param3 = {BABid, solution};
            int insertId = qRunner.insert(conn, "INSERT INTO Countermeasure(BABid, solution) values(?,?)", new ScalarHandler<BigDecimal>(), param3).intValue();//關閉線別

            PreparedStatement ps = conn.prepareStatement("INSERT INTO CountermeasureDetail(cm_id, ac_id) values(?,?)");

            for (String actionCode : actionCodes) {
                Object[] param = {insertId, actionCode};
                qRunner.fillStatement(ps, param);
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();

            Object[] param4 = {insertId, editor, "insert"};
            qRunner.update(conn, "INSERT INTO CountermeasureEvent(cm_id, editor, event) VALUES(?,?,?)", param4);

            DbUtils.commitAndCloseQuietly(conn);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn);
        }
        return flag;
    }

    public boolean updateCountermeasure(int BABid, String solution, List<String> actionCodes, String editor) {

        boolean flag = false;
        Connection conn = null;

        Countermeasure cm = this.getCountermeasure(BABid);

        try {
            QueryRunner qRunner = new QueryRunner();

            conn = this.getConn();
            conn.setAutoCommit(false);

            Object[] param3 = {solution, cm.getId()};
            qRunner.update(conn, "UPDATE Countermeasure SET solution = ? WHERE id = ?", param3);//關閉線別

            int cm_id = cm.getId();
            qRunner.update(conn, "DELETE FROM CountermeasureDetail WHERE cm_id = ?", cm_id);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO CountermeasureDetail(cm_id, ac_id) values(?,?)");

            for (String actionCode : actionCodes) {
                Object[] param = {cm_id, actionCode};
                qRunner.fillStatement(ps, param);
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();

            Object[] param4 = {cm_id, editor, "update"};
            qRunner.update(conn, "INSERT INTO CountermeasureEvent(cm_id, editor, event) VALUES(?,?,?)", param4);

            DbUtils.commitAndCloseQuietly(conn);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn);
        }
        return flag;
    }

    public boolean deleteCountermeasure(int id) {
        return updateCountermeasure("DELETE FROM Countermeasure WHERE BABid = ?", id);
    }

    private boolean updateCountermeasure(String sql, Object... params) {
        return update(getConn(), sql, params);
    }
}
