/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import java.io.Serializable;
import java.util.Objects;
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
public class MaterialPropertyUserPermission implements Serializable {

    @XmlElement(name = "USER_NAME_CH")
    private String userName;

    @XmlElement(name = "USER_ID")
    private int userId;

    @XmlElement(name = "MAT_USER_ID")
    private int materialUserId;

    @XmlElement(name = "MAT_PROPERTY_NO")
    private String materialPropertyNo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMaterialUserId() {
        return materialUserId;
    }

    public void setMaterialUserId(int materialUserId) {
        this.materialUserId = materialUserId;
    }

    public String getMaterialPropertyNo() {
        return materialPropertyNo;
    }

    public void setMaterialPropertyNo(String materialPropertyNo) {
        this.materialPropertyNo = materialPropertyNo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.userName);
        hash = 17 * hash + Objects.hashCode(this.materialPropertyNo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MaterialPropertyUserPermission other = (MaterialPropertyUserPermission) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.materialPropertyNo, other.materialPropertyNo)) {
            return false;
        }
        return true;
    }

}
