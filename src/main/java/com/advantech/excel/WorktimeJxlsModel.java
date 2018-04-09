/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.excel;

import com.advantech.model.BusinessGroup;
import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.model.User;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Wei.Cheng New "one to many object" in hibernate pojo(Worktime.class)
 * have heavy performing issue Create other reader to retrive worktime's data.
 * This object is as same as Worktime.class
 */
public class WorktimeJxlsModel {

    private int id;
    private Floor floor = new Floor();
    private Flow flowByTestFlowId = new Flow();
    private Flow flowByPackingFlowId = new Flow();
    private Flow flowByBabFlowId = new Flow();
    private User userByEeOwnerId = new User();
    private User userByQcOwnerId = new User();
    private User userBySpeOwnerId = new User();
    private Pending pending = new Pending();
    private PreAssy preAssy = new PreAssy();
    private Type type = new Type();
    private BusinessGroup businessGroup = new BusinessGroup();
    private String modelName;

    private String workCenter;
    private BigDecimal totalModule = BigDecimal.ZERO;
    private BigDecimal cleanPanel = BigDecimal.ZERO;
    private BigDecimal assy = BigDecimal.ZERO;
    private BigDecimal t1 = BigDecimal.ZERO;
    private BigDecimal t2 = BigDecimal.ZERO;
    private BigDecimal t3 = BigDecimal.ZERO;
    private BigDecimal t4 = BigDecimal.ZERO;
    private BigDecimal packing = BigDecimal.ZERO;
    private BigDecimal upBiRi = BigDecimal.ZERO;
    private BigDecimal downBiRi = BigDecimal.ZERO;
    private BigDecimal biCost = BigDecimal.ZERO;
    private BigDecimal vibration = BigDecimal.ZERO;
    private BigDecimal hiPotLeakage = BigDecimal.ZERO;
    private BigDecimal coldBoot = BigDecimal.ZERO;
    private BigDecimal warmBoot = BigDecimal.ZERO;
    private BigDecimal pendingTime;
    private String burnIn;
    private BigDecimal biTime;
    private BigDecimal biTemperature;
    private String assyPackingSop;
    private String testSop;
    private Integer keypartA = 0;
    private Integer keypartB = 0;
    private Character partLink;
    private int ce;
    private int ul;
    private int rohs;
    private int weee;
    private int madeInTaiwan;
    private int fcc;
    private int eac;
    private int kc;
    private BigDecimal nsInOneCollectionBox = BigDecimal.ZERO;
    private char partNoAttributeMaintain;
    private BigDecimal weight = BigDecimal.ZERO;
    private BigDecimal tolerance = BigDecimal.ZERO;
    private BigDecimal assyLeadTime = BigDecimal.ZERO;
    private BigDecimal packingLeadTime = BigDecimal.ZERO;
    private BigDecimal productionWt = BigDecimal.ZERO;
    private BigDecimal setupTime = BigDecimal.ZERO;
    private BigDecimal assyToT1 = BigDecimal.ZERO;
    private BigDecimal t2ToPacking = BigDecimal.ZERO;
    private Integer assyStation = 0;
    private Integer packingStation = 0;
    private BigDecimal assyKanbanTime = BigDecimal.ZERO;
    private BigDecimal packingKanbanTime = BigDecimal.ZERO;
    private BigDecimal cleanPanelAndAssembly = BigDecimal.ZERO;
//    private Date createDate;
    private Date modifiedDate;

    public WorktimeJxlsModel() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Flow getFlowByTestFlowId() {
        return this.flowByTestFlowId;
    }

    public void setFlowByTestFlowId(Flow flowByTestFlowId) {
        this.flowByTestFlowId = flowByTestFlowId;
    }

    public Flow getFlowByPackingFlowId() {
        return this.flowByPackingFlowId;
    }

    public void setFlowByPackingFlowId(Flow flowByPackingFlowId) {
        this.flowByPackingFlowId = flowByPackingFlowId;
    }

    public Flow getFlowByBabFlowId() {
        return this.flowByBabFlowId;
    }

    public void setFlowByBabFlowId(Flow flowByBabFlowId) {
        this.flowByBabFlowId = flowByBabFlowId;
    }

    public User getUserByEeOwnerId() {
        return this.userByEeOwnerId;
    }

    public void setUserByEeOwnerId(User userByEeOwnerId) {
        this.userByEeOwnerId = userByEeOwnerId;
    }

    public User getUserByQcOwnerId() {
        return this.userByQcOwnerId;
    }

    public void setUserByQcOwnerId(User userByQcOwnerId) {
        this.userByQcOwnerId = userByQcOwnerId;
    }

    public User getUserBySpeOwnerId() {
        return this.userBySpeOwnerId;
    }

    public void setUserBySpeOwnerId(User userBySpeOwnerId) {
        this.userBySpeOwnerId = userBySpeOwnerId;
    }

    public Pending getPending() {
        return this.pending;
    }

    public void setPending(Pending pending) {
        this.pending = pending;
    }

    public PreAssy getPreAssy() {
        return this.preAssy;
    }

    public void setPreAssy(PreAssy preAssy) {
        this.preAssy = preAssy;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(String workCenter) {
        this.workCenter = workCenter;
    }

    public BigDecimal getTotalModule() {
        return this.totalModule;
    }

    public void setTotalModule(BigDecimal totalModule) {
        this.totalModule = totalModule;
    }

    public BigDecimal getCleanPanel() {
        return this.cleanPanel;
    }

    public void setCleanPanel(BigDecimal cleanPanel) {
        this.cleanPanel = cleanPanel;
    }

    public BigDecimal getAssy() {
        return this.assy;
    }

    public void setAssy(BigDecimal assy) {
        this.assy = assy;
    }

    public BigDecimal getT1() {
        return this.t1;
    }

    public void setT1(BigDecimal t1) {
        this.t1 = t1;
    }

    public BigDecimal getT2() {
        return this.t2;
    }

    public void setT2(BigDecimal t2) {
        this.t2 = t2;
    }

    public BigDecimal getT3() {
        return this.t3;
    }

    public void setT3(BigDecimal t3) {
        this.t3 = t3;
    }

    public BigDecimal getT4() {
        return this.t4;
    }

    public void setT4(BigDecimal t4) {
        this.t4 = t4;
    }

    public BigDecimal getPacking() {
        return this.packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getUpBiRi() {
        return this.upBiRi;
    }

    public void setUpBiRi(BigDecimal upBiRi) {
        this.upBiRi = upBiRi;
    }

    public BigDecimal getDownBiRi() {
        return this.downBiRi;
    }

    public void setDownBiRi(BigDecimal downBiRi) {
        this.downBiRi = downBiRi;
    }

    public BigDecimal getBiCost() {
        return this.biCost;
    }

    public void setBiCost(BigDecimal biCost) {
        this.biCost = biCost;
    }

    public BigDecimal getVibration() {
        return this.vibration;
    }

    public void setVibration(BigDecimal vibration) {
        this.vibration = vibration;
    }

    public BigDecimal getHiPotLeakage() {
        return this.hiPotLeakage;
    }

    public void setHiPotLeakage(BigDecimal hiPotLeakage) {
        this.hiPotLeakage = hiPotLeakage;
    }

    public BigDecimal getColdBoot() {
        return this.coldBoot;
    }

    public void setColdBoot(BigDecimal coldBoot) {
        this.coldBoot = coldBoot;
    }

    public BigDecimal getWarmBoot() {
        return this.warmBoot;
    }

    public void setWarmBoot(BigDecimal warmBoot) {
        this.warmBoot = warmBoot;
    }

    public BigDecimal getPendingTime() {
        return this.pendingTime;
    }

    public void setPendingTime(BigDecimal pendingTime) {
        this.pendingTime = pendingTime;
    }

    public String getBurnIn() {
        return this.burnIn;
    }

    public void setBurnIn(String burnIn) {
        this.burnIn = burnIn;
    }

    public BigDecimal getBiTime() {
        return this.biTime;
    }

    public void setBiTime(BigDecimal biTime) {
        this.biTime = biTime;
    }

    public BigDecimal getBiTemperature() {
        return this.biTemperature;
    }

    public void setBiTemperature(BigDecimal biTemperature) {
        this.biTemperature = biTemperature;
    }

    public String getAssyPackingSop() {
        return this.assyPackingSop != null ? ("".equals(this.assyPackingSop.trim()) ? null : this.assyPackingSop.trim()) : null;
    }

    public void setAssyPackingSop(String assyPackingSop) {
        this.assyPackingSop = assyPackingSop;
    }

    public String getTestSop() {
        return this.testSop != null ? ("".equals(this.testSop.trim()) ? null : this.testSop.trim()) : null;
    }

    public void setTestSop(String testSop) {
        this.testSop = testSop;
    }

    public Integer getKeypartA() {
        return this.keypartA;
    }

    public void setKeypartA(Integer keypartA) {
        this.keypartA = keypartA;
    }

    public Integer getKeypartB() {
        return this.keypartB;
    }

    public void setKeypartB(Integer keypartB) {
        this.keypartB = keypartB;
    }

    public Character getPartLink() {
        return this.partLink;
    }

    public void setPartLink(Character partLink) {
        this.partLink = partLink;
    }

    public int getCe() {
        return this.ce;
    }

    public void setCe(int ce) {
        this.ce = ce;
    }

    public int getUl() {
        return this.ul;
    }

    public void setUl(int ul) {
        this.ul = ul;
    }

    public int getRohs() {
        return this.rohs;
    }

    public void setRohs(int rohs) {
        this.rohs = rohs;
    }

    public int getWeee() {
        return this.weee;
    }

    public void setWeee(int weee) {
        this.weee = weee;
    }

    public int getMadeInTaiwan() {
        return this.madeInTaiwan;
    }

    public void setMadeInTaiwan(int madeInTaiwan) {
        this.madeInTaiwan = madeInTaiwan;
    }

    public int getFcc() {
        return this.fcc;
    }

    public void setFcc(int fcc) {
        this.fcc = fcc;
    }

    public int getEac() {
        return this.eac;
    }

    public void setEac(int eac) {
        this.eac = eac;
    }

    public int getKc() {
        return this.kc;
    }

    public void setKc(int kc) {
        this.kc = kc;
    }

    public BigDecimal getNsInOneCollectionBox() {
        return this.nsInOneCollectionBox;
    }

    public void setNsInOneCollectionBox(BigDecimal NsInOneCollectionBox) {
        this.nsInOneCollectionBox = NsInOneCollectionBox;
    }

    public char getPartNoAttributeMaintain() {
        return this.partNoAttributeMaintain;
    }

    public void setPartNoAttributeMaintain(char partNoAttributeMaintain) {
        this.partNoAttributeMaintain = partNoAttributeMaintain;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = tolerance;
    }

    public BigDecimal getAssyLeadTime() {
        return this.assyLeadTime;
    }

    public void setAssyLeadTime(BigDecimal assyLeadTime) {
        this.assyLeadTime = assyLeadTime;
    }

    public BigDecimal getPackingLeadTime() {
        return this.packingLeadTime;
    }

    public void setPackingLeadTime(BigDecimal packingLeadTime) {
        this.packingLeadTime = packingLeadTime;
    }

    public BigDecimal getProductionWt() {
        return productionWt;
    }

    public void setProductionWt(BigDecimal productionWt) {
        this.productionWt = productionWt;
    }

    public BigDecimal getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(BigDecimal setupTime) {
        this.setupTime = setupTime;
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

    public Integer getAssyStation() {
        return assyStation;
    }

    public void setAssyStation(Integer assyStation) {
        this.assyStation = assyStation;
    }

    public Integer getPackingStation() {
        return packingStation;
    }

    public void setPackingStation(Integer packingStation) {
        this.packingStation = packingStation;
    }

    public BigDecimal getAssyKanbanTime() {
        return assyKanbanTime;
    }

    public void setAssyKanbanTime(BigDecimal assyKanbanTime) {
        this.assyKanbanTime = assyKanbanTime;
    }

    public BigDecimal getPackingKanbanTime() {
        return packingKanbanTime;
    }

    public void setPackingKanbanTime(BigDecimal packingKanbanTime) {
        this.packingKanbanTime = packingKanbanTime;
    }

    public BigDecimal getCleanPanelAndAssembly() {
        return cleanPanelAndAssembly;
    }

    public void setCleanPanelAndAssembly(BigDecimal cleanPanelAndAssembly) {
        this.cleanPanelAndAssembly = cleanPanelAndAssembly;
    }

//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
