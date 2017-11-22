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
public class MaterialFlow implements Serializable {

    @XmlElement(name = "MF_ID")
    private int id;

    @XmlElement(name = "ITEM_ID")
    private int itemId;

    @XmlElement(name = "ITEM_NO")
    private String itemNo;

    @XmlElement(name = "FLOW_RULE_ID")
    private int flowRuleId;

    @XmlElement(name = "FLOW_RULE_NAME")
    private String flowRuleName;

    @XmlElement(name = "UNIT_NO")
    private String unitNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getFlowRuleId() {
        return flowRuleId;
    }

    public void setFlowRuleId(int flowRuleId) {
        this.flowRuleId = flowRuleId;
    }

    public String getFlowRuleName() {
        return flowRuleName;
    }

    public void setFlowRuleName(String flowRuleName) {
        this.flowRuleName = flowRuleName;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

}
