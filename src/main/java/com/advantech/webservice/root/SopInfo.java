/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.root;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "QryData")
public class SopInfo implements Serializable {

    @XmlElement(name = "SOP_NAME")
    private String sopName;

    @XmlElement(name = "STATION_NO")
    private String stationNo;

    @XmlElement(name = "SOP_PAGE_NO")
    private String sopPageNo;

    public String getSopName() {
        return sopName;
    }

    public void setSopName(String sopName) {
        this.sopName = sopName;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getSopPageNo() {
        return sopPageNo;
    }

    public void setSopPageNo(String sopPageNo) {
        this.sopPageNo = sopPageNo;
    }

}
