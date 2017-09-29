/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.root;

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
public class SopInfos implements Serializable, QueryResult<SopInfo> {

    @XmlElement(name = "QryData", type = SopInfo.class)
    private List<SopInfo> QryData;

    @Override
    public List<SopInfo> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<SopInfo> QryData) {
        this.QryData = QryData;
    }

}
