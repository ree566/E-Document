/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
//@Entity
public class TagNameComparison implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orginTagName;
    private String lampSysTagName;
    private Integer lineId;
    private Integer stationId;

    public TagNameComparison() {

    }

    public TagNameComparison(String orginTagName, String lampSysTagName, Integer lineId, Integer stationId) {
        this.orginTagName = orginTagName;
        this.lampSysTagName = lampSysTagName;
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public String getOrginTagName() {
        return orginTagName;
    }

    public void setOrginTagName(String orginTagName) {
        this.orginTagName = orginTagName;
    }

    public String getLampSysTagName() {
        return lampSysTagName;
    }

    public void setLampSysTagName(String lampSysTagName) {
        this.lampSysTagName = lampSysTagName;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

}
