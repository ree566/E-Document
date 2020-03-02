/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

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
public class TestRecords implements Serializable{

    @XmlElement(name = "QryData", type = TestRecord.class)
    private List<TestRecord> QryData;


    public List<TestRecord> getQryData() {
        return QryData;
    }

    public void setQryData(List<TestRecord> QryData) {
        this.QryData = QryData;
    }

}
