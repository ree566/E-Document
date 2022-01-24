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
 * @author MFG.ESOP
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class MtdTestIntegritys  implements Serializable, QueryResult<MtdTestIntegrity>{

    @XmlElement(name = "QryData", type = MtdTestIntegrity.class)
    private List<MtdTestIntegrity> QryData;

    @Override
    public List<MtdTestIntegrity> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<MtdTestIntegrity> QryData) {
        this.QryData = QryData;
    }
}
