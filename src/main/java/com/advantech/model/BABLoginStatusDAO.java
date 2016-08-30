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

    public boolean babLogin(int BABid, int station, String jobnumber) {
        return update(getConn(), "INSERT INTO babLoginStatus(babid, station, jobnumber) VALUES(?,?,?)", BABid, station, jobnumber);
    }

    public boolean changeUser(int BABid, int station, String jobnumber) {
        return update(getConn(), "UPDATE babLoginStatus SET jobnumber = ? WHERE babid = ? AND station = ?", jobnumber, BABid, station);
    }
    
    public boolean deleteUserFromStation(int BABid, int station){
        return update(getConn(), "DELETE FROM babLoginStatus WHERE babid = ? AND station = ?", BABid, station);
    }

    public BABPeopleRecord getExistUserInBAB(int BABid, int station) {
        List l = queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE BABid = ? AND station = ?",
                BABid, station
        );
        return !l.isEmpty() ? (BABPeopleRecord) l.get(0) : null;
    }

    public List<BABPeopleRecord> getExistUserInBAB(int BABid) {
        return queryForBeanList(
                getConn(),
                BABPeopleRecord.class,
                "SELECT * FROM BABPeopleRecord WHERE BABid = ?",
                BABid
        );
    }

    public boolean recordBABPeople(List<BABPeopleRecord> l) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(BABid, station, user_id) VALUES (?,?,?)",
                l,
                "BABid", "station", "user_id");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }

}
