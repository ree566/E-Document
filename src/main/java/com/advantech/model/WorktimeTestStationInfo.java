/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author MFG.ESOP
 */
@Entity
@Table(name = "Worktime_TestStationInfo")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorktimeTestStationInfo implements java.io.Serializable {

    private int id;
    private Worktime worktime;
    private String stationName;
    private int statusCnt;
    private int itemsCnt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worktime_id", nullable = false)
    public Worktime getWorktime() {
        return worktime;
    }

    public void setWorktime(Worktime worktime) {
        this.worktime = worktime;
    }

    @Column(name = "station_name", nullable = false)
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Column(name = "statusCnt", nullable = false)
    public int getStatusCnt() {
        return statusCnt;
    }

    public void setStatusCnt(int statusCnt) {
        this.statusCnt = statusCnt;
    }

    @Column(name = "itemsCnt", nullable = false)
    public int getItemsCnt() {
        return itemsCnt;
    }

    public void setItemsCnt(int itemsCnt) {
        this.itemsCnt = itemsCnt;
    }
    
    
}
