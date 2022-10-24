package com.advantech.model;
// Generated 2017/4/7 下午 02:26:06 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.AutoPopulatingList;

/**
 * Worktime generated by hbm2java
 * http://www.cnblogs.com/chenssy/archive/2012/09/09/2677279.html How to use
 * BigDecimal
 */
@Entity
@Table(name = "Worktime",
        uniqueConstraints = @UniqueConstraint(columnNames = "model_name")
)
@DynamicInsert(true)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Worktime.class)
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
public class Worktime implements java.io.Serializable {

    @JsonView(View.Public.class)
    private int id;

    @JsonView(View.Public.class)
    private String modelName;

    @JsonView(View.Public.class)
    private Pending pending;

    @JsonView(View.Public.class)
    private BigDecimal pendingTime;

    @JsonView(View.Public.class)
    private Type type;

    @JsonView(View.Public.class)
    private BusinessGroup businessGroup;

    @JsonView(View.Public.class)
    private WorkCenter workCenter;

    @JsonView(View.Public.class)
    private BigDecimal productionWt = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal setupTime = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal arFilmAttachment = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal seal = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal opticalBonding = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private String pressureCooker;

    @JsonView(View.Public.class)
    private BigDecimal cleanPanel = BigDecimal.ZERO;
    
    @JsonView(View.Public.class)
    private BigDecimal machineWorktime = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal pi = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal assy = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal highBright = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal bondedSealingFrame = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal t1 = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal t2 = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal t3 = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal packing = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal upBiRi = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal downBiRi = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal biCost = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal assyToT1 = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal t2ToPacking = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private Floor floor;

    @JsonView(View.Public.class)
    private String burnIn;

    @JsonView(View.Public.class)
    private BigDecimal biTime;

    @JsonView(View.Public.class)
    private BigDecimal biTemperature;

    @JsonView(View.Public.class)
    private User userBySpeOwnerId;

    @JsonView(View.Public.class)
    private User userByEeOwnerId; //Unit name change "EE" to "BPE"

    @JsonView(View.Public.class)
    private User userByQcOwnerId;

    @JsonView(View.Public.class)
    private User userByMpmOwnerId;

    @JsonView(View.Public.class)
    private String assyPackingSop;

    @JsonView(View.Public.class)
    private String testSop;

    @JsonView(View.Public.class)
    private Integer keypartA = 0;

    @JsonView(View.Public.class)
    private Integer keypartB = 0;

    @JsonView(View.Public.class)
    private PreAssy preAssy;

    @JsonView(View.Public.class)
    private Flow flowByTestFlowId;

    @JsonView(View.Public.class)
    private Flow flowByBabFlowId;

    @JsonView(View.Public.class)
    private Flow flowByPackingFlowId;

    @JsonView(View.Public.class)
    private String partLink;

    @JsonView(View.Public.class)
    private Remark remark;

    @JsonView(View.Public.class)
    private int ce;

    @JsonView(View.Public.class)
    private int ul;

    @JsonView(View.Public.class)
    private int rohs;

    @JsonView(View.Public.class)
    private int weee;

    @JsonView(View.Public.class)
    private int madeInTaiwan;

    @JsonView(View.Public.class)
    private int fcc;

    @JsonView(View.Public.class)
    private int eac;

    @JsonView(View.Public.class)
    private int kc;

    @JsonView(View.Public.class)
    private BigDecimal nsInOneCollectionBox = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private String partNoAttributeMaintain;

    @JsonView(View.Public.class)
    private String labelInformation;

    @JsonView(View.Public.class)
    private BigDecimal weight = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal tolerance = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal materialVolumeA = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal materialVolumeB = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal assyLeadTime = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private BigDecimal test = BigDecimal.ZERO;

    @JsonView(View.Public.class)
    private Date createDate;

    @JsonView(View.Public.class)
    private Date modifiedDate;

    @JsonView(View.Public.class)
    private List<BwField> bwField = new AutoPopulatingList<BwField>(BwField.class);

    @JsonView(View.Internal.class)
    private List<WorktimeFormulaSetting> worktimeFormulaSettings = new AutoPopulatingList<WorktimeFormulaSetting>(WorktimeFormulaSetting.class);

    @JsonView(View.Public.class)
    private String hrcValues;

    @JsonView(View.Internal.class)
    private Set<Cobot> cobots = new HashSet<Cobot>(0);

    public Worktime() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "model_name", unique = true, nullable = false, length = 50)
    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pending_id", nullable = false)
    public Pending getPending() {
        return pending;
    }

    public void setPending(Pending pending) {
        this.pending = pending;
    }

    @NotNull(message = "Pending Time 不可為空")
    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "pending_time", nullable = false, precision = 10, scale = 1)
    public BigDecimal getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(BigDecimal pendingTime) {
        this.pendingTime = pendingTime;
    }

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[type_id]", nullable = false)
    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessGroup_id")
    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workCenter_id")
    public WorkCenter getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(WorkCenter workCenter) {
        this.workCenter = workCenter;
    }

    @Digits(integer = 10 /*precision*/, fraction = 2 /*scale*/)
    @Column(name = "productionWt", precision = 10, scale = 2)
    public BigDecimal getProductionWt() {
        return productionWt;
    }

    public void setProductionWt(BigDecimal productionWt) {
        this.productionWt = autoFixScale(productionWt, 2);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "setup_time", precision = 10, scale = 1)
    public BigDecimal getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(BigDecimal setupTime) {
        this.setupTime = autoFixScale(setupTime, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "arFilmAttachment", precision = 10, scale = 1)
    public BigDecimal getArFilmAttachment() {
        return arFilmAttachment;
    }

    public void setArFilmAttachment(BigDecimal arFilmAttachment) {
        this.arFilmAttachment = autoFixScale(arFilmAttachment, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "seal", precision = 10, scale = 1)
    public BigDecimal getSeal() {
        return seal;
    }

    public void setSeal(BigDecimal seal) {
        this.seal = autoFixScale(seal, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "opticalBonding", precision = 10, scale = 1)
    public BigDecimal getOpticalBonding() {
        return opticalBonding;
    }

    public void setOpticalBonding(BigDecimal opticalBonding) {
        this.opticalBonding = autoFixScale(opticalBonding, 1);
    }

    @Size(min = 0, max = 10)
    @Column(name = "pressureCooker", length = 10)
    public String getPressureCooker() {
        return pressureCooker;
    }

    public void setPressureCooker(String pressureCooker) {
        this.pressureCooker = pressureCooker;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "clean_panel", precision = 10, scale = 1)
    public BigDecimal getCleanPanel() {
        return this.cleanPanel;
    }

    public void setCleanPanel(BigDecimal cleanPanel) {
        this.cleanPanel = autoFixScale(cleanPanel, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 2 /*scale*/)
    @Column(name = "machine_worktime", precision = 10, scale = 2)
    public BigDecimal getMachineWorktime() {
        return machineWorktime;
    }

    public void setMachineWorktime(BigDecimal machineWorktime) {
        this.machineWorktime = machineWorktime;
    }
    
    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "[pi]", precision = 10, scale = 1)
    public BigDecimal getPi() {
        return pi;
    }

    public void setPi(BigDecimal pi) {
        this.pi = autoFixScale(pi, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "assy", precision = 10, scale = 1)
    public BigDecimal getAssy() {
        return this.assy;
    }

    public void setAssy(BigDecimal assy) {
        this.assy = autoFixScale(assy, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "high_bright", precision = 10, scale = 1)
    public BigDecimal getHighBright() {
        return highBright;
    }

    public void setHighBright(BigDecimal highBright) {
        this.highBright = highBright;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "bonded_sealing_frame", precision = 10, scale = 1)
    public BigDecimal getBondedSealingFrame() {
        return bondedSealingFrame;
    }

    public void setBondedSealingFrame(BigDecimal bondedSealingFrame) {
        this.bondedSealingFrame = bondedSealingFrame;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "t1", precision = 10, scale = 1)
    public BigDecimal getT1() {
        return this.t1;
    }

    public void setT1(BigDecimal t1) {
        this.t1 = autoFixScale(t1, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "t2", precision = 10, scale = 1)
    public BigDecimal getT2() {
        return this.t2;
    }

    public void setT2(BigDecimal t2) {
        this.t2 = autoFixScale(t2, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "t3", precision = 10, scale = 1)
    public BigDecimal getT3() {
        return t3;
    }

    public void setT3(BigDecimal t3) {
        this.t3 = t3;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "packing", precision = 10, scale = 1)
    public BigDecimal getPacking() {
        return this.packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = autoFixScale(packing, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "up_bi_ri", precision = 10, scale = 1)
    public BigDecimal getUpBiRi() {
        return this.upBiRi;
    }

    public void setUpBiRi(BigDecimal upBiRi) {
        this.upBiRi = autoFixScale(upBiRi, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "down_bi_ri", precision = 10, scale = 1)
    public BigDecimal getDownBiRi() {
        return this.downBiRi;
    }

    public void setDownBiRi(BigDecimal downBiRi) {
        this.downBiRi = autoFixScale(downBiRi, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 2 /*scale*/)
    @Column(name = "bi_cost", precision = 10, scale = 2)
    public BigDecimal getBiCost() {
        return this.biCost;
    }

    public void setBiCost(BigDecimal biCost) {
        this.biCost = autoFixScale(biCost, 2);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "assy_to_t1", precision = 10, scale = 1)
    public BigDecimal getAssyToT1() {
        return assyToT1;
    }

    public void setAssyToT1(BigDecimal assyToT1) {
        this.assyToT1 = autoFixScale(assyToT1, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "t2_to_packing", precision = 10, scale = 1)
    public BigDecimal getT2ToPacking() {
        return t2ToPacking;
    }

    public void setT2ToPacking(BigDecimal t2ToPacking) {
        this.t2ToPacking = autoFixScale(t2ToPacking, 1);
    }

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @NotNull
    @NotEmpty
    @Column(name = "burn_in", nullable = false, length = 10)
    public String getBurnIn() {
        return this.burnIn;
    }

    public void setBurnIn(String burnIn) {
        this.burnIn = burnIn;
    }

    @NotNull(message = "Bi Time 不可為空")
    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "bi_time", nullable = false, precision = 10, scale = 1)
    public BigDecimal getBiTime() {
        return this.biTime;
    }

    public void setBiTime(BigDecimal biTime) {
        this.biTime = autoFixScale(biTime, 1);
    }

    @NotNull(message = "Bi Temperature 不可為空")
    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "bi_temperature", nullable = false, precision = 10, scale = 1)
    public BigDecimal getBiTemperature() {
        return this.biTemperature;
    }

    public void setBiTemperature(BigDecimal biTemperature) {
        this.biTemperature = autoFixScale(biTemperature, 1);
    }

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spe_owner_id", nullable = false)
    public User getUserBySpeOwnerId() {
        return this.userBySpeOwnerId;
    }

    public void setUserBySpeOwnerId(User userBySpeOwnerId) {
        this.userBySpeOwnerId = userBySpeOwnerId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ee_owner_id", nullable = false)
    public User getUserByEeOwnerId() {
        return this.userByEeOwnerId;
    }

    public void setUserByEeOwnerId(User userByEeOwnerId) {
        this.userByEeOwnerId = userByEeOwnerId;
    }

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qc_owner_id", nullable = false)
    public User getUserByQcOwnerId() {
        return this.userByQcOwnerId;
    }

    public void setUserByQcOwnerId(User userByQcOwnerId) {
        this.userByQcOwnerId = userByQcOwnerId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mpm_owner_id", nullable = true)
    public User getUserByMpmOwnerId() {
        return userByMpmOwnerId;
    }

    public void setUserByMpmOwnerId(User userByMpmOwnerId) {
        this.userByMpmOwnerId = userByMpmOwnerId;
    }

    @Size(min = 0, max = 500)
    @Column(name = "assy_packing_sop", length = 500)
    public String getAssyPackingSop() {
        return this.assyPackingSop != null ? ("".equals(this.assyPackingSop.trim()) ? null : this.assyPackingSop.trim()) : null;
    }

    public void setAssyPackingSop(String assyPackingSop) {
        this.assyPackingSop = assyPackingSop;
    }

    @Size(min = 0, max = 500)
    @Column(name = "test_sop", length = 500)
    public String getTestSop() {
        return this.testSop != null ? ("".equals(this.testSop.trim()) ? null : this.testSop.trim()) : null;
    }

    public void setTestSop(String testSop) {
        this.testSop = testSop;
    }

    @Column(name = "keypart_a")
    public Integer getKeypartA() {
        return this.keypartA;
    }

    public void setKeypartA(Integer keypartA) {
        this.keypartA = keypartA;
    }

    @Column(name = "keypart_b")
    public Integer getKeypartB() {
        return this.keypartB;
    }

    public void setKeypartB(Integer keypartB) {
        this.keypartB = keypartB;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_assy_id", nullable = true)
    public PreAssy getPreAssy() {
        return this.preAssy;
    }

    public void setPreAssy(PreAssy preAssy) {
        this.preAssy = preAssy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bab_flow_id", nullable = true)
    public Flow getFlowByBabFlowId() {
        return this.flowByBabFlowId;
    }

    public void setFlowByBabFlowId(Flow flowByBabFlowId) {
        this.flowByBabFlowId = flowByBabFlowId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_flow_id", nullable = true)
    public Flow getFlowByTestFlowId() {
        return this.flowByTestFlowId;
    }

    public void setFlowByTestFlowId(Flow flowByTestFlowId) {
        this.flowByTestFlowId = flowByTestFlowId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packing_flow_id", nullable = true)
    public Flow getFlowByPackingFlowId() {
        return this.flowByPackingFlowId;
    }

    public void setFlowByPackingFlowId(Flow flowByPackingFlowId) {
        this.flowByPackingFlowId = flowByPackingFlowId;
    }

    @Column(name = "part_link", length = 10)
    public String getPartLink() {
        return this.partLink;
    }

    public void setPartLink(String partLink) {
        this.partLink = partLink;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remark_id", nullable = true)
    public Remark getRemark() {
        return remark;
    }

    public void setRemark(Remark remark) {
        this.remark = remark;
    }

    @NotNull
    @Column(name = "ce", nullable = false)
    public int getCe() {
        return this.ce;
    }

    public void setCe(int ce) {
        this.ce = ce;
    }

    @NotNull
    @Column(name = "ul", nullable = false)
    public int getUl() {
        return this.ul;
    }

    public void setUl(int ul) {
        this.ul = ul;
    }

    @NotNull
    @Column(name = "rohs", nullable = false)
    public int getRohs() {
        return this.rohs;
    }

    public void setRohs(int rohs) {
        this.rohs = rohs;
    }

    @NotNull
    @Column(name = "weee", nullable = false)
    public int getWeee() {
        return this.weee;
    }

    public void setWeee(int weee) {
        this.weee = weee;
    }

    @NotNull
    @Column(name = "made_in_taiwan", nullable = false)
    public int getMadeInTaiwan() {
        return this.madeInTaiwan;
    }

    public void setMadeInTaiwan(int madeInTaiwan) {
        this.madeInTaiwan = madeInTaiwan;
    }

    @NotNull
    @Column(name = "fcc", nullable = false)
    public int getFcc() {
        return this.fcc;
    }

    public void setFcc(int fcc) {
        this.fcc = fcc;
    }

    @NotNull
    @Column(name = "eac", nullable = false)
    public int getEac() {
        return this.eac;
    }

    public void setEac(int eac) {
        this.eac = eac;
    }

    @NotNull
    @Column(name = "kc", nullable = false)
    public int getKc() {
        return this.kc;
    }

    public void setKc(int kc) {
        this.kc = kc;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "ns_in_one_collection_box", precision = 10, scale = 1)
    public BigDecimal getNsInOneCollectionBox() {
        return this.nsInOneCollectionBox;
    }

    public void setNsInOneCollectionBox(BigDecimal NsInOneCollectionBox) {
        this.nsInOneCollectionBox = autoFixScale(NsInOneCollectionBox, 1);
    }

    @Column(name = "part_no_attribute_maintain", length = 10)
    public String getPartNoAttributeMaintain() {
        return partNoAttributeMaintain;
    }

    public void setPartNoAttributeMaintain(String partNoAttributeMaintain) {
        this.partNoAttributeMaintain = partNoAttributeMaintain;
    }

    @Column(name = "label_information", length = 10)
    public String getLabelInformation() {
        return labelInformation;
    }

    public void setLabelInformation(String labelInformation) {
        this.labelInformation = labelInformation;
    }

    @NotNull
    @Digits(integer = 10 /*precision*/, fraction = 4 /*scale*/)
    @Column(name = "[weight]", nullable = false, precision = 10, scale = 4)
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = autoFixScale(weight, 4);
    }

    @NotNull
    @Digits(integer = 10 /*precision*/, fraction = 4 /*scale*/)
    @Column(name = "tolerance", nullable = false, precision = 10, scale = 4)
    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = autoFixScale(tolerance, 4);
    }

    @Column(name = "materialVolumeA")
    public BigDecimal getMaterialVolumeA() {
        return materialVolumeA;
    }

    public void setMaterialVolumeA(BigDecimal materialVolumeA) {
        this.materialVolumeA = materialVolumeA;
    }

    @Column(name = "materialVolumeB")
    public BigDecimal getMaterialVolumeB() {
        return materialVolumeB;
    }

    public void setMaterialVolumeB(BigDecimal materialVolumeB) {
        this.materialVolumeB = materialVolumeB;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "assy_lead_time", precision = 10, scale = 1)
    public BigDecimal getAssyLeadTime() {
        return this.assyLeadTime;
    }

    public void setAssyLeadTime(BigDecimal assyLeadTime) {
        this.assyLeadTime = autoFixScale(assyLeadTime, 1);
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "test", precision = 10, scale = 1)
    public BigDecimal getTest() {
        return test;
    }

    public void setTest(BigDecimal test) {
        this.test = autoFixScale(test, 1);
    }

    @NotAudited
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 23, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @NotAudited
    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", length = 23)
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    private BigDecimal autoFixScale(BigDecimal d, int scale) {
        return d == null ? null : d.setScale(scale, RoundingMode.HALF_UP);
    }

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "worktime", orphanRemoval = true)
    public List<WorktimeFormulaSetting> getWorktimeFormulaSettings() {
        return this.worktimeFormulaSettings;
    }

    public void setWorktimeFormulaSettings(List<WorktimeFormulaSetting> worktimeFormulaSettings) {
        this.worktimeFormulaSettings = worktimeFormulaSettings;
    }

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "worktime")
    public List<BwField> getBwFields() {
        return bwField;
    }

    public void setBwFields(List<BwField> bwFields) {
        this.bwField = bwFields;
    }

    @Size(min = 0, max = 200)
    @Column(name = "hrc_values", length = 200)
    public String getHrcValues() {
        return hrcValues;
    }

    public void setHrcValues(String hrcValues) {
        this.hrcValues = hrcValues;
    }

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Worktime_Cobot_REF", joinColumns = {
        @JoinColumn(name = "worktime_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "cobot_id", nullable = false, insertable = false, updatable = false)})
    public Set<Cobot> getCobots() {
        return cobots;
    }

    public void setCobots(Set<Cobot> cobots) {
        this.cobots = cobots;
    }

//  Default formula column caculate
    public void setDefaultProductWt() {
        BigDecimal defaultValue = notEmpty(arFilmAttachment).add(notEmpty(seal))
                .add(notEmpty(opticalBonding)).add(notEmpty(cleanPanel)).add(notEmpty(pi))
                .add(notEmpty(assy)).add(notEmpty(highBright)).add(notEmpty(bondedSealingFrame))
                .add(notEmpty(t1)).add(notEmpty(t2)).add(notEmpty(t3)).add(notEmpty(packing))
                .add(notEmpty(upBiRi)).add(notEmpty(downBiRi)).add(notEmpty(biCost));
        this.setProductionWt(defaultValue);
    }

    public void setDefaultSetupTime() {
        BigDecimal defaultValue = BigDecimal.ZERO
                .add(notEmpty(arFilmAttachment).add(notEmpty(cleanPanel)).add(notEmpty(pi)).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : new BigDecimal(20))
                .add(notEmpty(assy).add(notEmpty(highBright)).add(notEmpty(bondedSealingFrame)).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : new BigDecimal(10))
                .add(notEmpty(t1).add(notEmpty(t2)).add(notEmpty(t3)).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : new BigDecimal(5))
                .add(notEmpty(packing).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : new BigDecimal(10));
        this.setSetupTime(defaultValue);
    }

    public void setDefaultAssyToT1() {
        BigDecimal defaultValue = notEmpty(arFilmAttachment)
                .add(notEmpty(seal))
                .add(notEmpty(opticalBonding))
                .add(notEmpty(cleanPanel)).add(notEmpty(pi))
                .add(notEmpty(assy)).add(notEmpty(highBright)).add(notEmpty(bondedSealingFrame))
                .add(notEmpty(t1));
        this.setAssyToT1(defaultValue);
    }

    public void setDefaultT2ToPacking() {
        BigDecimal defaultValue = notEmpty(t2)
                .add(notEmpty(t3))
                .add(notEmpty(packing));
        this.setT2ToPacking(defaultValue);
    }

    public void setDefaultAssyLeadTime() {
        BigDecimal defaultValue = notEmpty(arFilmAttachment)
                .add(notEmpty(cleanPanel))
                .add(notEmpty(pi));
        this.setAssyLeadTime(defaultValue);
    }

    public void setDefaultTest() {
        BigDecimal defaultValue = notEmpty(t1).add(notEmpty(t2)).add(notEmpty(t3));
        this.setTest(defaultValue);
    }

    private BigDecimal notEmpty(BigDecimal d) {
        return d == null ? BigDecimal.ZERO : d;
    }

    //Override hashcode and equals will force audit query eager loading the one to many field
    //Still looking for reason.
}
