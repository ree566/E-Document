/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

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
public class StandardWorkTimes implements Serializable, QueryResult<StandardWorkTime> {

    @XmlElement(name = "QryData", type = StandardWorkTime.class)
    private List<StandardWorkTime> QryData;

    @Override
    public List<StandardWorkTime> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<StandardWorkTime> QryData) {
        this.QryData = QryData;
    }

}