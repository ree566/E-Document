/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

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
public class MaterialPropertyValue {

    @XmlElement(name = "ITEM_NO")
    private String itemNo;

    @XmlElement(name = "MAT_PROPERTY_NO")
    private String matPropertyNo;

    @XmlElement(name = "MAT_PROPERTY_DESC")
    private String matPropertyName;

    @XmlElement(name = "VALUE")
    private String value;

    @XmlElement(name = "AFF_PRO_TYPE")
    private String affPropertyType;

    @XmlElement(name = "AFF_VALUE")
    private String affPropertyValue;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getMatPropertyNo() {
        return matPropertyNo;
    }

    public void setMatPropertyNo(String matPropertyNo) {
        this.matPropertyNo = matPropertyNo;
    }

    public String getMatPropertyName() {
        return matPropertyName;
    }

    public void setMatPropertyName(String matPropertyName) {
        this.matPropertyName = matPropertyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAffPropertyType() {
        return affPropertyType;
    }

    public void setAffPropertyType(String affPropertyType) {
        this.affPropertyType = affPropertyType;
    }

    public String getAffPropertyValue() {
        return affPropertyValue;
    }

    public void setAffPropertyValue(String affPropertyValue) {
        this.affPropertyValue = affPropertyValue;
    }

}
