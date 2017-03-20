/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.helper.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Sheet_Main_view",
        schema = "dbo",
        catalog = "E_Document"
)
public class SheetView implements Serializable {

    @Id
    @Column(name = "modelId", unique = true, nullable = false)
    private int modelId;

    @Column(name = "modelName", nullable = false, length = 50)
    private String modelName;

    @Column(name = "typeId")
    private Integer typeId;

    @Column(name = "typeName")
    private String typeName;

    @Column(name = "ProductionWT", precision = 10, scale = 1)
    private BigDecimal ProductionWT;

    @Column(name = "totalModule", precision = 10, scale = 1)
    private BigDecimal totalModule;

    @Column(name = "SetupTime")
    private Integer SetupTime;

    @Column(name = "cleanPanel", precision = 10, scale = 1)
    private BigDecimal cleanPanel;

    @Column(name = "assy", precision = 10, scale = 1)
    private BigDecimal assy;

    @Column(name = "t1", precision = 10, scale = 1)
    private BigDecimal t1;

    @Column(name = "t2", precision = 10, scale = 1)
    private BigDecimal t2;

    @Column(name = "t3", precision = 10, scale = 1)
    private BigDecimal t3;

    @Column(name = "t4", precision = 10, scale = 1)
    private BigDecimal t4;

    @Column(name = "packing", precision = 10, scale = 1)
    private BigDecimal packing;

    @Column(name = "upBiRi", precision = 10, scale = 1)
    private BigDecimal upBiRi;

    @Column(name = "downBiRi", precision = 10, scale = 1)
    private BigDecimal downBiRi;

    @Column(name = "biCost", precision = 10, scale = 1)
    private BigDecimal biCost;

    @Column(name = "vibration")
    private Integer vibration;

    @Column(name = "hiPotLeakage")
    private Integer hiPotLeakage;

    @Column(name = "coldBoot", precision = 10, scale = 1)
    private BigDecimal coldBoot;

    @Column(name = "warmBoot", precision = 10, scale = 1)
    private BigDecimal warmBoot;

    @Column(name = "assyToT1", precision = 10, scale = 1)
    private BigDecimal assyToT1;

    @Column(name = "t2ToPacking", precision = 10, scale = 1)
    private BigDecimal t2ToPacking;

    @Column(name = "floorId")
    private Integer floorId;

    @Column(name = "floorName")
    private String floorName;

    @Column(name = "pendingType")
    private String pendingType;

    @Column(name = "pendingTime", precision = 10, scale = 1)
    private BigDecimal pendingTime;

    @Column(name = "burnIn")
    private String burnIn;

    @Column(name = "biTime", precision = 10, scale = 1)
    private BigDecimal biTime;

    @Column(name = "biTemperature", precision = 10, scale = 1)
    private BigDecimal biTemperature;

    @Column(name = "speOwnerId")
    private String speOwnerId;

    @Column(name = "speOwnerName")
    private String speOwnerName;

    @Column(name = "eeOwnerId")
    private String eeOwnerId;

    @Column(name = "eeOwnerName")
    private String eeOwnerName;

    @Column(name = "qcOwnerId")
    private String qcOwnerId;

    @Column(name = "qcOwnerName")
    private String qcOwnerName;

    @Column(name = "assyPackingSop")
    private String assyPackingSop;

    @Column(name = "testSop")
    private String testSop;

    @Column(name = "keypartA")
    private Integer keypartA;

    @Column(name = "keypartB")
    private Integer keypartB;

    @Column(name = "preAssy")
    private String preAssy;

    @Column(name = "babFlow")
    private String babFlow;

    @Column(name = "testFlow")
    private String testFlow;

    @Column(name = "packingFlow")
    private String packingFlow;

    @Column(name = "partLink")
    private String partLink;

    @Column(name = "ce")
    private String ce;

    @Column(name = "ul")
    private String ul;

    @Column(name = "rohs")
    private String rohs;

    @Column(name = "weee")
    private String weee;

    @Column(name = "madeInTaiwan")
    private String madeInTaiwan;

    @Column(name = "fcc")
    private String fcc;

    @Column(name = "eac")
    private String eac;

    @Column(name = "nIn1CollectionBox")
    private String nIn1CollectionBox;

    @Column(name = "partNoAttrMaintain")
    private String partNoAttrMaintain;

    @Column(name = "assyStations")
    private Integer assyStations;

    @Column(name = "packingStations")
    private Integer packingStations;

    @Column(name = "assyLeadTime", precision = 10, scale = 1)
    private BigDecimal assyLeadTime;

    @Column(name = "assyKanbanTime", precision = 10, scale = 1)
    private BigDecimal assyKanbanTime;

    @Column(name = "packingLeadTime", precision = 10, scale = 1)
    private BigDecimal packingLeadTime;

    @Column(name = "packingKanbanTime", precision = 10, scale = 1)
    private BigDecimal packingKanbanTime;

    @Column(name = "CleanPanel_and_Assembly", precision = 10, scale = 1)
    private BigDecimal CleanPanel_and_Assembly;

//    @JsonSerialize(using = JsonDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Modified_Date", length = 23, updatable = false)
    private Date modified_Date;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getProductionWT() {
        return ProductionWT;
    }

    public void setProductionWT(BigDecimal ProductionWT) {
        this.ProductionWT = ProductionWT;
    }

    public BigDecimal getTotalModule() {
        return totalModule;
    }

    public void setTotalModule(BigDecimal totalModule) {
        this.totalModule = totalModule;
    }

    public Integer getSetupTime() {
        return SetupTime;
    }

    public void setSetupTime(Integer SetupTime) {
        this.SetupTime = SetupTime;
    }

    public BigDecimal getCleanPanel() {
        return cleanPanel;
    }

    public void setCleanPanel(BigDecimal cleanPanel) {
        this.cleanPanel = cleanPanel;
    }

    public BigDecimal getAssy() {
        return assy;
    }

    public void setAssy(BigDecimal assy) {
        this.assy = assy;
    }

    public BigDecimal getT1() {
        return t1;
    }

    public void setT1(BigDecimal t1) {
        this.t1 = t1;
    }

    public BigDecimal getT2() {
        return t2;
    }

    public void setT2(BigDecimal t2) {
        this.t2 = t2;
    }

    public BigDecimal getT3() {
        return t3;
    }

    public void setT3(BigDecimal t3) {
        this.t3 = t3;
    }

    public BigDecimal getT4() {
        return t4;
    }

    public void setT4(BigDecimal t4) {
        this.t4 = t4;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getUpBiRi() {
        return upBiRi;
    }

    public void setUpBiRi(BigDecimal upBiRi) {
        this.upBiRi = upBiRi;
    }

    public BigDecimal getDownBiRi() {
        return downBiRi;
    }

    public void setDownBiRi(BigDecimal downBiRi) {
        this.downBiRi = downBiRi;
    }

    public BigDecimal getBiCost() {
        return biCost;
    }

    public void setBiCost(BigDecimal biCost) {
        this.biCost = biCost;
    }

    public Integer getVibration() {
        return vibration;
    }

    public void setVibration(Integer vibration) {
        this.vibration = vibration;
    }

    public Integer getHiPotLeakage() {
        return hiPotLeakage;
    }

    public void setHiPotLeakage(Integer hiPotLeakage) {
        this.hiPotLeakage = hiPotLeakage;
    }

    public BigDecimal getColdBoot() {
        return coldBoot;
    }

    public void setColdBoot(BigDecimal coldBoot) {
        this.coldBoot = coldBoot;
    }

    public BigDecimal getWarmBoot() {
        return warmBoot;
    }

    public void setWarmBoot(BigDecimal warmBoot) {
        this.warmBoot = warmBoot;
    }

    public BigDecimal getAssyToT1() {
        return assyToT1;
    }

    public void setAssyToT1(BigDecimal assyToT1) {
        this.assyToT1 = assyToT1;
    }

    public BigDecimal getT2ToPacking() {
        return t2ToPacking;
    }

    public void setT2ToPacking(BigDecimal t2ToPacking) {
        this.t2ToPacking = t2ToPacking;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getPendingType() {
        return pendingType;
    }

    public void setPendingType(String pendingType) {
        this.pendingType = pendingType;
    }

    public BigDecimal getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(BigDecimal pendingTime) {
        this.pendingTime = pendingTime;
    }

    public String getBurnIn() {
        return burnIn;
    }

    public void setBurnIn(String burnIn) {
        this.burnIn = burnIn;
    }

    public BigDecimal getBiTime() {
        return biTime;
    }

    public void setBiTime(BigDecimal biTime) {
        this.biTime = biTime;
    }

    public BigDecimal getBiTemperature() {
        return biTemperature;
    }

    public void setBiTemperature(BigDecimal biTemperature) {
        this.biTemperature = biTemperature;
    }

    public String getSpeOwnerId() {
        return speOwnerId;
    }

    public void setSpeOwnerId(String speOwnerId) {
        this.speOwnerId = speOwnerId;
    }

    public String getSpeOwnerName() {
        return speOwnerName;
    }

    public void setSpeOwnerName(String speOwnerName) {
        this.speOwnerName = speOwnerName;
    }

    public String getEeOwnerId() {
        return eeOwnerId;
    }

    public void setEeOwnerId(String eeOwnerId) {
        this.eeOwnerId = eeOwnerId;
    }

    public String getEeOwnerName() {
        return eeOwnerName;
    }

    public void setEeOwnerName(String eeOwnerName) {
        this.eeOwnerName = eeOwnerName;
    }

    public String getQcOwnerId() {
        return qcOwnerId;
    }

    public void setQcOwnerId(String qcOwnerId) {
        this.qcOwnerId = qcOwnerId;
    }

    public String getQcOwnerName() {
        return qcOwnerName;
    }

    public void setQcOwnerName(String qcOwnerName) {
        this.qcOwnerName = qcOwnerName;
    }

    public String getAssyPackingSop() {
        return assyPackingSop;
    }

    public void setAssyPackingSop(String assyPackingSop) {
        this.assyPackingSop = assyPackingSop;
    }

    public String getTestSop() {
        return testSop;
    }

    public void setTestSop(String testSop) {
        this.testSop = testSop;
    }

    public Integer getKeypartA() {
        return keypartA;
    }

    public void setKeypartA(Integer keypartA) {
        this.keypartA = keypartA;
    }

    public Integer getKeypartB() {
        return keypartB;
    }

    public void setKeypartB(Integer keypartB) {
        this.keypartB = keypartB;
    }

    public String getPreAssy() {
        return preAssy;
    }

    public void setPreAssy(String preAssy) {
        this.preAssy = preAssy;
    }

    public String getBabFlow() {
        return babFlow;
    }

    public void setBabFlow(String babFlow) {
        this.babFlow = babFlow;
    }

    public String getTestFlow() {
        return testFlow;
    }

    public void setTestFlow(String testFlow) {
        this.testFlow = testFlow;
    }

    public String getPackingFlow() {
        return packingFlow;
    }

    public void setPackingFlow(String packingFlow) {
        this.packingFlow = packingFlow;
    }

    public String getPartLink() {
        return partLink;
    }

    public void setPartLink(String partLink) {
        this.partLink = partLink;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    public String getRohs() {
        return rohs;
    }

    public void setRohs(String rohs) {
        this.rohs = rohs;
    }

    public String getWeee() {
        return weee;
    }

    public void setWeee(String weee) {
        this.weee = weee;
    }

    public String getFcc() {
        return fcc;
    }

    public void setFcc(String fcc) {
        this.fcc = fcc;
    }

    public String getEac() {
        return eac;
    }

    public void setEac(String eac) {
        this.eac = eac;
    }

    public String getMadeInTaiwan() {
        return madeInTaiwan;
    }

    public void setMadeInTaiwan(String madeInTaiwan) {
        this.madeInTaiwan = madeInTaiwan;
    }

    public String getnIn1CollectionBox() {
        return nIn1CollectionBox;
    }

    public void setnIn1CollectionBox(String nIn1CollectionBox) {
        this.nIn1CollectionBox = nIn1CollectionBox;
    }

    public String getPartNoAttrMaintain() {
        return partNoAttrMaintain;
    }

    public void setPartNoAttrMaintain(String partNoAttrMaintain) {
        this.partNoAttrMaintain = partNoAttrMaintain;
    }

    public Integer getAssyStations() {
        return assyStations;
    }

    public void setAssyStations(Integer assyStations) {
        this.assyStations = assyStations;
    }

    public Integer getPackingStations() {
        return packingStations;
    }

    public void setPackingStations(Integer packingStations) {
        this.packingStations = packingStations;
    }

    public BigDecimal getAssyLeadTime() {
        return assyLeadTime;
    }

    public void setAssyLeadTime(BigDecimal assyLeadTime) {
        this.assyLeadTime = assyLeadTime;
    }

    public BigDecimal getAssyKanbanTime() {
        return assyKanbanTime;
    }

    public void setAssyKanbanTime(BigDecimal assyKanbanTime) {
        this.assyKanbanTime = assyKanbanTime;
    }

    public BigDecimal getPackingLeadTime() {
        return packingLeadTime;
    }

    public void setPackingLeadTime(BigDecimal packingLeadTime) {
        this.packingLeadTime = packingLeadTime;
    }

    public BigDecimal getPackingKanbanTime() {
        return packingKanbanTime;
    }

    public void setPackingKanbanTime(BigDecimal packingKanbanTime) {
        this.packingKanbanTime = packingKanbanTime;
    }

    public BigDecimal getCleanPanel_and_Assembly() {
        return CleanPanel_and_Assembly;
    }

    public void setCleanPanel_and_Assembly(BigDecimal CleanPanel_and_Assembly) {
        this.CleanPanel_and_Assembly = CleanPanel_and_Assembly;
    }

    public Date getModified_Date() {
        return modified_Date;
    }

    public void setModified_Date(Date modified_Date) {
        this.modified_Date = modified_Date;
    }

}
