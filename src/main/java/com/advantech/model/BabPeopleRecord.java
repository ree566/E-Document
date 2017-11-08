/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
public class BabPeopleRecord implements Serializable {

    private int id;
    private int BABid;
    private int station;
    private String user_id;
    private String btime;

    public BabPeopleRecord() {
    }

    public BabPeopleRecord(int BABid, int station, String user_id) {
        this.BABid = BABid;
        this.station = station;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBABid() {
        return BABid;
    }

    public void setBABid(int BABid) {
        this.BABid = BABid;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

}
