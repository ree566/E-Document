/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import com.advantech.webservice.root.QueryResult;
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
public class MaterialPropertys implements Serializable, QueryResult<MaterialProperty> {

    @XmlElement(name = "QryData", type = MaterialProperty.class)
    private List<MaterialProperty> QryData;

    @Override
    public List<MaterialProperty> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<MaterialProperty> QryData) {
        this.QryData = QryData;
    }

}