/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;

/**
 *
 * @author MFG.ESOP
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "QryData")
public class MtdTestIntegrity {

    @XmlElement(name = "DUT_PART_NO")
    private String modelName;

    @XmlElement(name = "STATION_NAME")
    private String stationName;

    @XmlElement(name = "TOTALSTATE")
    private int stateCnt;

    @XmlElement(name = "TOTALTESTITEM")
    private int itemCnt;

    @XmlElement(name = "USER_NAME_CH")
    private String userName;

    @XmlElement(name = "UPDATE_DATE")
    private DateTime updateDate;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStateCnt() {
        return stateCnt;
    }

    public void setStateCnt(int stateCnt) {
        this.stateCnt = stateCnt;
    }

    public int getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(int itemCnt) {
        this.itemCnt = itemCnt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
    }

}
