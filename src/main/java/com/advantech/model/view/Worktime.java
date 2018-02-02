/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Wei.Cheng
 * vw_worktime
 */
@Entity
@Immutable
@Table(name = "vw_WorkTime")
public class Worktime implements Serializable {

    private String modelName;
    private String floorName;
    private String speOwnerName;
    private String eeOwnerName;
    private String qcOwnerName;
    private BigDecimal assyTime;
    private BigDecimal t1Time;
    private BigDecimal t2Time;
    private BigDecimal packingTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "modelName", unique = true, updatable = false)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name = "floorName", updatable = false)
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Column(name = "speOwnerName", updatable = false)
    public String getSpeOwnerName() {
        return speOwnerName;
    }

    public void setSpeOwnerName(String speOwnerName) {
        this.speOwnerName = speOwnerName;
    }

    @Column(name = "eeOwnerName", updatable = false)
    public String getEeOwnerName() {
        return eeOwnerName;
    }

    public void setEeOwnerName(String eeOwnerName) {
        this.eeOwnerName = eeOwnerName;
    }

    @Column(name = "qcOwnerName", updatable = false)
    public String getQcOwnerName() {
        return qcOwnerName;
    }

    public void setQcOwnerName(String qcOwnerName) {
        this.qcOwnerName = qcOwnerName;
    }

    @Column(name = "assyTime", updatable = false)
    public BigDecimal getAssyTime() {
        return assyTime;
    }

    public void setAssyTime(BigDecimal assyTime) {
        this.assyTime = assyTime;
    }

    @Column(name = "t1Time", updatable = false)
    public BigDecimal getT1Time() {
        return t1Time;
    }

    public void setT1Time(BigDecimal t1Time) {
        this.t1Time = t1Time;
    }

    @Column(name = "t2Time", updatable = false)
    public BigDecimal getT2Time() {
        return t2Time;
    }

    public void setT2Time(BigDecimal t2Time) {
        this.t2Time = t2Time;
    }

    @Column(name = "packingTime", updatable = false)
    public BigDecimal getPackingTime() {
        return packingTime;
    }

    public void setPackingTime(BigDecimal packingTime) {
        this.packingTime = packingTime;
    }

}
