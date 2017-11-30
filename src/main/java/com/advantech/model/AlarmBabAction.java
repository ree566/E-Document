/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Alm_BabAction")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tableId")
public class AlarmBabAction implements AlarmAction, Serializable {

    private String tableId;
    private int alarm;
    private Date lastUpdateTime;

    public AlarmBabAction() {
    }

    public AlarmBabAction(String tableId, int alarm) {
        this.tableId = tableId;
        this.alarm = alarm;
    }

    @Id
    @Column(name = "tableId", unique = true, nullable = false)
    @Override
    public String getTableId() {
        return tableId;
    }

    @Override
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Column(name = "alarm", nullable = false)
    @Override
    public int getAlarm() {
        return alarm;
    }

    @Override
    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime", length = 23, insertable = false, updatable = false)
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
