/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.BABPeopleRecord;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BABLoginStatusDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BABLoginStatusDAO.class);

    public BABLoginStatusDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<BABLoginStatus> queryFBNTable(String sql, Object... params) {
        return queryForBeanList(getConn(), BABLoginStatus.class, sql, params);
    }

    public List<BABLoginStatus> getBABLoginStatus() {
        return queryFBNTable("SELECT * FROM BABLoginStatus");
    }

    public BABLoginStatus getUser(String jobnumber) {
        List l = queryFBNTable("SELECT * FROM BABLoginStatus WHERE jobnumber = ?", jobnumber);
        return !l.isEmpty() ? (BABLoginStatus) l.get(0) : null;
    }

    public List<BABLoginStatus> getBABLoginStatus(int lineId) {
        return queryFBNTable("SELECT * FROM BABLoginStatus WHERE lineId = ?", lineId);
    }

    public BABLoginStatus getBABLoginStatus(int lineId, int station) {
        List l = queryFBNTable("SELECT * FROM BABLoginStatus WHERE lineId = ? AND station = ?", lineId, station);
        return !l.isEmpty() ? (BABLoginStatus) l.get(0) : null;
    }

    public boolean firstStationBABLogin(int lineNo, String jobnumber) {

        boolean flag = false;
        Connection conn = null;

        try {
            conn = this.getConn();
            conn.setAutoCommit(false);
            QueryRunner qRunner = new QueryRunner();

            Object[] params = {lineNo, 1, jobnumber};
            qRunner.update(conn, "INSERT INTO babLoginStatus(lineId, station, jobnumber) VALUES(?,?,?)", params);

            Object[] params2 = {1, lineNo};
            qRunner.update(conn, "UPDATE LS_Line SET isused = ? WHERE id = ?", params2);

            DbUtils.commitAndCloseQuietly(conn);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn);
        }
        return flag;
    }

    public boolean firstStationBABLogout(int lineNo) {
        boolean flag = false;
        Connection conn = null;

        try {
            conn = this.getConn();
            conn.setAutoCommit(false);
            QueryRunner qRunner = new QueryRunner();

            Object[] params = {lineNo, 1};
            qRunner.update(conn, "DELETE FROM babLoginStatus WHERE lineId = ? AND station = ?", params);

            Object[] params2 = {0, lineNo};
            qRunner.update(conn, "UPDATE LS_Line SET isused = ? WHERE id = ?", params2);

            DbUtils.commitAndCloseQuietly(conn);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn);
        }
        return flag;
    }

    public boolean babLogin(int lineId, int station, String jobnumber) {
        return update(getConn(), "INSERT INTO babLoginStatus(lineId, station, jobnumber) VALUES(?,?,?)", lineId, station, jobnumber);
    }

    public boolean changeUser(int lineId, int station, String jobnumber) {
        return update(getConn(), "UPDATE babLoginStatus SET jobnumber = ? WHERE lineId = ? AND station = ?", jobnumber, lineId, station);
    }

    public boolean deleteUserFromStation(int lineId, int station) {
        return update(getConn(), "DELETE FROM babLoginStatus WHERE lineId = ? AND station = ?", lineId, station);
    }

    public BABPeopleRecord getBABPeopleRecord(int lineId, int station) {
        List l = queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE lineId = ? AND station = ?",
                lineId, station
        );
        return !l.isEmpty() ? (BABPeopleRecord) l.get(0) : null;
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int lineId) {
        return queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE lineId = ?",
                lineId
        );
    }

    public boolean recordBABPeople(BABPeopleRecord b) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(BABid, station, user_id) VALUES (?,?,?)",
                b.getBABid(), b.getStation(), b.getUser_id());
    }

    public boolean recordBABPeople(List<BABPeopleRecord> l) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(BABid, station, user_id) VALUES (?,?,?)",
                l,
                "BABid", "station", "user_id");
    }
}
