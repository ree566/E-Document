/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.BABPeopleRecord;
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

    private List<BABLoginStatus> queryBABLoginStatus(String sql, Object... params) {
        return queryForBeanList(getConn(), BABLoginStatus.class, sql, params);
    }

    private List<BABPeopleRecord> queryBABPeopleRecord(String sql, Object... params) {
        return queryForBeanList(getConn(), BABPeopleRecord.class, sql, params);
    }

    public List<BABLoginStatus> getBABLoginStatus() {
        return queryBABLoginStatus("SELECT * FROM BABLoginStatus");
    }

    public boolean babLogin(int lineId, int station, String jobnumber) {
        return update(getConn(), "INSERT INTO babLoginStatus(babid, station, jobnumber) VALUES(?,?,?)", lineId, station, jobnumber);
    }

    public boolean changeUser(int lineId, int station, String jobnumber) {
        return update(getConn(), "UPDATE babLoginStatus SET jobnumber = ? WHERE babid = ? AND station = ?", jobnumber, lineId, station);
    }

    public boolean deleteUserFromStation(int lineId, int station) {
        return update(getConn(), "DELETE FROM babLoginStatus WHERE babid = ? AND station = ?", lineId, station);
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int BABid, int station) {
        return queryBABPeopleRecord("SELECT * FROM BABPeopleRecord WHERE BABid = ? AND station = ? ORDER BY ID", BABid, station);
    }

    public List<BABPeopleRecord> getBABPeopleRecord(int BABid) {
        return queryBABPeopleRecord("SELECT * FROM BABPeopleRecord WHERE BABid = ?", BABid);
    }

    public boolean recordBABPeople(List<BABPeopleRecord> l) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(BABid, station, user_id) VALUES (?,?,?)",
                l,
                "BABid", "station", "user_id");
    }

}
