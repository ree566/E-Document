/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class TestPassStationDetails implements Serializable{

    @XmlElement(name = "QryData", type = TestPassStationDetail.class)
    private List<TestPassStationDetail> QryData;


    public List<TestPassStationDetail> getQryData() {
        return QryData;
    }

    public void setQryData(List<TestPassStationDetail> QryData) {
        this.QryData = QryData;
    }

}
