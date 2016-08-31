/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.BABPeopleRecord;
import static com.advantech.model.BasicDAO.queryForBeanList;
import static com.advantech.model.BasicDAO.update;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class BABLoginStatusDAO extends BasicDAO {

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

    public BABLoginStatus getBABLoginStatus(int lineId, int station) {
        List l = queryFBNTable("SELECT * FROM BABLoginStatus WHERE lineId = ? AND station = ?", lineId, station);
        return !l.isEmpty() ? (BABLoginStatus) l.get(0) : null;
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

    public BABPeopleRecord getExistUserInBAB(int lineId, int station) {
        List l = queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE lineId = ? AND station = ?",
                lineId, station
        );
        return !l.isEmpty() ? (BABPeopleRecord) l.get(0) : null;
    }

    public List<BABPeopleRecord> getExistUserInBAB(int lineId) {
        return queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE lineId = ?",
                lineId
        );
    }

    public boolean recordBABPeople(List<BABPeopleRecord> l) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(lineId, station, user_id) VALUES (?,?,?)",
                l,
                "lineId", "station", "user_id");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }

}
