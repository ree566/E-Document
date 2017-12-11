/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Wei.Cheng
 */
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getSpeOwnerName() {
        return speOwnerName;
    }

    public void setSpeOwnerName(String speOwnerName) {
        this.speOwnerName = speOwnerName;
    }

    public String getEeOwnerName() {
        return eeOwnerName;
    }

    public void setEeOwnerName(String eeOwnerName) {
        this.eeOwnerName = eeOwnerName;
    }

    public String getQcOwnerName() {
        return qcOwnerName;
    }

    public void setQcOwnerName(String qcOwnerName) {
        this.qcOwnerName = qcOwnerName;
    }

    public BigDecimal getAssyTime() {
        return assyTime;
    }

    public void setAssyTime(BigDecimal assyTime) {
        this.assyTime = assyTime;
    }

    public BigDecimal getT1Time() {
        return t1Time;
    }

    public void setT1Time(BigDecimal t1Time) {
        this.t1Time = t1Time;
    }

    public BigDecimal getT2Time() {
        return t2Time;
    }

    public void setT2Time(BigDecimal t2Time) {
        this.t2Time = t2Time;
    }

    public BigDecimal getPackingTime() {
        return packingTime;
    }

    public void setPackingTime(BigDecimal packingTime) {
        this.packingTime = packingTime;
    }

}
