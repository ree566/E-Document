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
public class MaterialPropertyUserPermissions implements Serializable, QueryResult<MaterialPropertyUserPermission> {

    @XmlElement(name = "QryMatPropertyUser002", type = MaterialPropertyUserPermission.class)
    private List<MaterialPropertyUserPermission> QryData;

    @Override
    public List<MaterialPropertyUserPermission> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<MaterialPropertyUserPermission> QryData) {
        this.QryData = QryData;
    }
}
