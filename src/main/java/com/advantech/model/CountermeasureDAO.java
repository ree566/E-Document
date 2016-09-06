/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Countermeasure;
import com.advantech.entity.ErrorCode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
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

    public List<Countermeasure> getCountermeasure(int BABid) {
        return this.queryCountermeasureTable("SELECT * FROM Countermeasure WHERE BABid = ?", BABid);
    }

    public List getErrorCode() {
        return queryForArrayList(getConn(), "SELECT * FROM errorCode");
    }

    public List getActionCode() {
        return queryForArrayList(getConn(), "SELECT * FROM actionCode");
    }

    public boolean insertCountermeasure(int BABid, String solution, List<ErrorCode> errorCodes, String editor) {

        boolean flag = false;
        Connection conn = null;

        try {
            QueryRunner qRunner = new QueryRunner();

            conn = this.getConn();
            conn.setAutoCommit(false);

            Object[] param3 = {BABid, solution};
//            qRunner.insert(conn, "INSERT INTO Countermeasure(BABid, solution) values(?,?)", param3);//關閉線別

            PreparedStatement ps = conn.prepareStatement("INSERT INTO CountermeasureDetail(cm_id, ec_id, ac_id) values(?,?,?)");

            for (ErrorCode errorCode : errorCodes) {
                Object[] param = {};
                qRunner.fillStatement(ps, param);
                
            }

            ps.executeBatch();
            ps.close();

            DbUtils.commitAndCloseQuietly(conn);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn);

        }
        return flag;

//        return updateCountermeasure("INSERT INTO Countermeasure(BABid, errorCode_id, reason, solution, editor) values(?,?,?,?,?)", BABid, errorCode_id, reason, solution, editor);
    }

    public boolean updateCountermeasure(int BABid, int errorCode_id, String reason, String solution, String editor) {
        return updateCountermeasure("UPDATE Countermeasure SET errorCode_id = ?, reason = ?, solution = ?, editor = ? WHERE BABid = ?", errorCode_id, reason, solution, editor, BABid);
    }

    public boolean deleteCountermeasure(int id) {
        return updateCountermeasure("DELETE FROM Countermeasure WHERE BABid = ?", id);
    }

    private boolean updateCountermeasure(String sql, Object... params) {
        return update(getConn(), sql, params);
    }
}
