/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Wei.Cheng
 * CPK issue
 */

public class TagNameComparison implements Serializable {

    private String orginTagName;

    private String lampSysTagName;

    private Integer lineId;

    private Integer stationId;
    
    private Integer defaultStationId;

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

    @XmlElement
    public void setOrginTagName(String orginTagName) {
        this.orginTagName = orginTagName;
    }

    public String getLampSysTagName() {
        return lampSysTagName;
    }

    @XmlElement
    public void setLampSysTagName(String lampSysTagName) {
        this.lampSysTagName = lampSysTagName;
    }

    public Integer getLineId() {
        return lineId;
    }

    @XmlElement
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getStationId() {
        return stationId;
    }

    @XmlElement
    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getDefaultStationId() {
        return defaultStationId;
    }

    @XmlElement
    public void setDefaultStationId(Integer defaultStationId) {
        this.defaultStationId = defaultStationId;
    }
    
    

}
