/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabLoginStatus;
import com.advantech.model.BabPeopleRecord;
import java.sql.Connection;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabLoginStatusDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<BabLoginStatus> queryBABLoginStatus(String sql, Object... params) {
        return queryForBeanList(getConn(), BabLoginStatus.class, sql, params);
    }

    private List<BabPeopleRecord> queryBABPeopleRecord(String sql, Object... params) {
        return queryForBeanList(getConn(), BabPeopleRecord.class, sql, params);
    }

    public List<BabLoginStatus> getBABLoginStatus() {
        return queryBABLoginStatus("SELECT * FROM BABLoginStatus");
    }

    public List<BabLoginStatus> getBABLoginStatus(String jobnumber) {
        return queryBABLoginStatus("SELECT * FROM BABLoginStatus WHERE jobnumber = ? ", jobnumber);
    }

    public List<BabLoginStatus> getBABLoginStatus(int lineId, int station) {
        return queryBABLoginStatus("SELECT * FROM BABLoginStatus WHERE lineId = ? AND station = ?", lineId, station);
    }

    public boolean insert(int lineId, int station, String jobnumber) {
        return update(getConn(), "INSERT INTO babLoginStatus(babid, station, jobnumber) VALUES(?,?,?)", lineId, station, jobnumber);
    }

    public boolean update(int lineId, int station, String jobnumber) {
        return update(getConn(), "UPDATE babLoginStatus SET jobnumber = ? WHERE babid = ? AND station = ?", jobnumber, lineId, station);
    }

    public boolean delete(int lineId, int station) {
        return update(getConn(), "DELETE FROM babLoginStatus WHERE babid = ? AND station = ?", lineId, station);
    }

    public List<BabPeopleRecord> getBABPeopleRecord(int BABid, int station) {
        return queryBABPeopleRecord("SELECT * FROM BABPeopleRecord WHERE BABid = ? AND station = ? ORDER BY ID", BABid, station);
    }

    public List<BabPeopleRecord> getBABPeopleRecord(int BABid) {
        return queryBABPeopleRecord("SELECT * FROM BABPeopleRecord WHERE BABid = ?", BABid);
    }

    public boolean recordBABPeople(List<BabPeopleRecord> l) {
        return update(getConn(),
                "INSERT INTO BABPeopleRecord(BABid, station, user_id) VALUES (?,?,?)",
                l,
                "BABid", "station", "user_id");
    }

}
