/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

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
public class StandardWorkReason implements Serializable {

    public StandardWorkReason() {
        throw new RuntimeException("Receive object \"StandardWorkReason\" not changed yet");
    }

    @XmlElement(name = "GROUP_NO")
    private String id;

    @XmlElement(name = "GROUP_NAME")
    private String name;

    public String getId() {
        return id == null ? null : id.trim();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
