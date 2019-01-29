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
public class StandardWorkReasons implements Serializable, QueryResult<StandardWorkReason> {

    @XmlElement(name = "QryData", type = StandardWorkReason.class)
    private List<StandardWorkReason> QryData;

    @Override
    public List<StandardWorkReason> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<StandardWorkReason> QryData) {
        this.QryData = QryData;
    }

}