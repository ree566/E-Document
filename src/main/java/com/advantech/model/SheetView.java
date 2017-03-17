/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(name = "Model_id", unique = true, nullable = false)
    private int Model_id;

    @Column(name = "Model", nullable = false, length = 50)
    private String Model;

    @Column(name = "Type_id")
    private Integer Type_id;

    @Column(name = "Type")
    private String Type;

    @Column(name = "ProductionWT", precision = 10, scale = 1)
    private BigDecimal ProductionWT;

    @Column(name = "Total_Module", precision = 10, scale = 1)
    private BigDecimal Total_Module;

    @Column(name = "SetupTime")
    private Integer SetupTime;

    @Column(name = "CleanPanel", precision = 10, scale = 1)
    private BigDecimal CleanPanel;

    @Column(name = "ASSY", precision = 10, scale = 1)
    private BigDecimal ASSY;

    @Column(name = "T1", precision = 10, scale = 1)
    private BigDecimal T1;

    @Column(name = "T2", precision = 10, scale = 1)
    private BigDecimal T2;

    @Column(name = "T3", precision = 10, scale = 1)
    private BigDecimal T3;

    @Column(name = "T4", precision = 10, scale = 1)
    private BigDecimal T4;

    @Column(name = "Packing", precision = 10, scale = 1)
    private BigDecimal Packing;

    @Column(name = "Up_BI_RI", precision = 10, scale = 1)
    private BigDecimal Up_BI_RI;

    @Column(name = "Down_BI_RI", precision = 10, scale = 1)
    private BigDecimal Down_BI_RI;

    @Column(name = "BI_Cost", precision = 10, scale = 1)
    private BigDecimal BI_Cost;

    @Column(name = "Vibration")
    private Integer Vibration;

    @Column(name = "Hi_Pot_Leakage")
    private Integer Hi_Pot_Leakage;

    @Column(name = "Cold_Boot", precision = 10, scale = 1)
    private BigDecimal Cold_Boot;

    @Column(name = "Warm_Boot", precision = 10, scale = 1)
    private BigDecimal Warm_Boot;

    @Column(name = "ASSY_to_T1", precision = 10, scale = 1)
    private BigDecimal ASSY_to_T1;

    @Column(name = "T2_to_Packing", precision = 10, scale = 1)
    private BigDecimal T2_to_Packing;

    @Column(name = "Floor_id")
    private Integer Floor_id;

    @Column(name = "Floor")
    private String Floor;

    @Column(name = "Pending")
    private String Pending;

    @Column(name = "Pending_Time", precision = 10, scale = 1)
    private BigDecimal Pending_Time;

    @Column(name = "BurnIn")
    private String BurnIn;

    @Column(name = "BI_Time", precision = 10, scale = 1)
    private BigDecimal BI_Time;

    @Column(name = "BI_Temperature", precision = 10, scale = 1)
    private BigDecimal BI_Temperature;

    @Column(name = "SPE_Owner")
    private String SPE_Owner;

    @Column(name = "EE_Owner")
    private String EE_Owner;

    @Column(name = "QC_Owner")
    private String QC_Owner;

    @Column(name = "ASSY_Packing_SOP")
    private String ASSY_Packing_SOP;

    @Column(name = "Test_SOP")
    private String Test_SOP;

    @Column(name = "Keypart_A")
    private Integer Keypart_A;

    @Column(name = "Keypart_B")
    private Integer Keypart_B;

    @Column(name = "Pre_ASSY")
    private String Pre_ASSY;

    @Column(name = "BAB_Flow")
    private String BAB_Flow;

    @Column(name = "Test_Flow")
    private String Test_Flow;

    @Column(name = "Packing_Flow")
    private String Packing_Flow;

    @Column(name = "Part_Link")
    private String Part_Link;

    @Column(name = "CE")
    private String CE;

    @Column(name = "UL")
    private String UL;

    @Column(name = "ROHS")
    private String ROHS;

    @Column(name = "WEEE")
    private String WEEE;

    @Column(name = "Made_in_Taiwan")
    private String Made_in_Taiwan;

    @Column(name = "FCC")
    private String FCC;

    @Column(name = "EAC")
    private String EAC;

    @Column(name = "N_in_1_collection_box")
    private String N_in_1_collection_box;

    @Column(name = "PartNo_attr_maintain")
    private String PartNo_attr_maintain;

    @Column(name = "ASSY_stations")
    private Integer ASSY_stations;

    @Column(name = "Packing_stations")
    private Integer Packing_stations;

    @Column(name = "ASSY_lead_time", precision = 10, scale = 1)
    private BigDecimal ASSY_lead_time;

    @Column(name = "ASSY_kanban_time", precision = 10, scale = 1)
    private BigDecimal ASSY_kanban_time;

    @Column(name = "Packing_lead_time", precision = 10, scale = 1)
    private BigDecimal Packing_lead_time;

    @Column(name = "Packing_kanban_time", precision = 10, scale = 1)
    private BigDecimal Packing_kanban_time;

    @Column(name = "CleanPanel_and_Assembly", precision = 10, scale = 1)
    private BigDecimal CleanPanel_and_Assembly;

    @Column(name = "Modified_Date")
    private Date Modified_Date;

    public int getModel_id() {
        return Model_id;
    }

    public void setModel_id(int Model_id) {
        this.Model_id = Model_id;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public Integer getType_id() {
        return Type_id;
    }

    public void setType_id(Integer Type_id) {
        this.Type_id = Type_id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public BigDecimal getProductionWT() {
        return ProductionWT;
    }

    public void setProductionWT(BigDecimal ProductionWT) {
        this.ProductionWT = ProductionWT;
    }

    public BigDecimal getTotal_Module() {
        return Total_Module;
    }

    public void setTotal_Module(BigDecimal Total_Module) {
        this.Total_Module = Total_Module;
    }

    public Integer getSetupTime() {
        return SetupTime;
    }

    public void setSetupTime(Integer SetupTime) {
        this.SetupTime = SetupTime;
    }

    public BigDecimal getCleanPanel() {
        return CleanPanel;
    }

    public void setCleanPanel(BigDecimal CleanPanel) {
        this.CleanPanel = CleanPanel;
    }

    public BigDecimal getASSY() {
        return ASSY;
    }

    public void setASSY(BigDecimal ASSY) {
        this.ASSY = ASSY;
    }

    public BigDecimal getT1() {
        return T1;
    }

    public void setT1(BigDecimal T1) {
        this.T1 = T1;
    }

    public BigDecimal getT2() {
        return T2;
    }

    public void setT2(BigDecimal T2) {
        this.T2 = T2;
    }

    public BigDecimal getT3() {
        return T3;
    }

    public void setT3(BigDecimal T3) {
        this.T3 = T3;
    }

    public BigDecimal getT4() {
        return T4;
    }

    public void setT4(BigDecimal T4) {
        this.T4 = T4;
    }

    public BigDecimal getPacking() {
        return Packing;
    }

    public void setPacking(BigDecimal Packing) {
        this.Packing = Packing;
    }

    public BigDecimal getUp_BI_RI() {
        return Up_BI_RI;
    }

    public void setUp_BI_RI(BigDecimal Up_BI_RI) {
        this.Up_BI_RI = Up_BI_RI;
    }

    public BigDecimal getDown_BI_RI() {
        return Down_BI_RI;
    }

    public void setDown_BI_RI(BigDecimal Down_BI_RI) {
        this.Down_BI_RI = Down_BI_RI;
    }

    public BigDecimal getBI_Cost() {
        return BI_Cost;
    }

    public void setBI_Cost(BigDecimal BI_Cost) {
        this.BI_Cost = BI_Cost;
    }

    public Integer getVibration() {
        return Vibration;
    }

    public void setVibration(Integer Vibration) {
        this.Vibration = Vibration;
    }

    public Integer getHi_Pot_Leakage() {
        return Hi_Pot_Leakage;
    }

    public void setHi_Pot_Leakage(Integer Hi_Pot_Leakage) {
        this.Hi_Pot_Leakage = Hi_Pot_Leakage;
    }

    public BigDecimal getCold_Boot() {
        return Cold_Boot;
    }

    public void setCold_Boot(BigDecimal Cold_Boot) {
        this.Cold_Boot = Cold_Boot;
    }

    public BigDecimal getWarm_Boot() {
        return Warm_Boot;
    }

    public void setWarm_Boot(BigDecimal Warm_Boot) {
        this.Warm_Boot = Warm_Boot;
    }

    public BigDecimal getASSY_to_T1() {
        return ASSY_to_T1;
    }

    public void setASSY_to_T1(BigDecimal ASSY_to_T1) {
        this.ASSY_to_T1 = ASSY_to_T1;
    }

    public BigDecimal getT2_to_Packing() {
        return T2_to_Packing;
    }

    public void setT2_to_Packing(BigDecimal T2_to_Packing) {
        this.T2_to_Packing = T2_to_Packing;
    }

    public Integer getFloor_id() {
        return Floor_id;
    }

    public void setFloor_id(Integer Floor_id) {
        this.Floor_id = Floor_id;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String Floor) {
        this.Floor = Floor;
    }

    public String getPending() {
        return Pending;
    }

    public void setPending(String Pending) {
        this.Pending = Pending;
    }

    public BigDecimal getPending_Time() {
        return Pending_Time;
    }

    public void setPending_Time(BigDecimal Pending_Time) {
        this.Pending_Time = Pending_Time;
    }

    public String getBurnIn() {
        return BurnIn;
    }

    public void setBurnIn(String BurnIn) {
        this.BurnIn = BurnIn;
    }

    public BigDecimal getBI_Time() {
        return BI_Time;
    }

    public void setBI_Time(BigDecimal BI_Time) {
        this.BI_Time = BI_Time;
    }

    public BigDecimal getBI_Temperature() {
        return BI_Temperature;
    }

    public void setBI_Temperature(BigDecimal BI_Temperature) {
        this.BI_Temperature = BI_Temperature;
    }

    public String getSPE_Owner() {
        return SPE_Owner;
    }

    public void setSPE_Owner(String SPE_Owner) {
        this.SPE_Owner = SPE_Owner;
    }

    public String getEE_Owner() {
        return EE_Owner;
    }

    public void setEE_Owner(String EE_Owner) {
        this.EE_Owner = EE_Owner;
    }

    public String getQC_Owner() {
        return QC_Owner;
    }

    public void setQC_Owner(String QC_Owner) {
        this.QC_Owner = QC_Owner;
    }

    public String getASSY_Packing_SOP() {
        return ASSY_Packing_SOP;
    }

    public void setASSY_Packing_SOP(String ASSY_Packing_SOP) {
        this.ASSY_Packing_SOP = ASSY_Packing_SOP;
    }

    public String getTest_SOP() {
        return Test_SOP;
    }

    public void setTest_SOP(String Test_SOP) {
        this.Test_SOP = Test_SOP;
    }

    public Integer getKeypart_A() {
        return Keypart_A;
    }

    public void setKeypart_A(Integer Keypart_A) {
        this.Keypart_A = Keypart_A;
    }

    public Integer getKeypart_B() {
        return Keypart_B;
    }

    public void setKeypart_B(Integer Keypart_B) {
        this.Keypart_B = Keypart_B;
    }

    public String getPre_ASSY() {
        return Pre_ASSY;
    }

    public void setPre_ASSY(String Pre_ASSY) {
        this.Pre_ASSY = Pre_ASSY;
    }

    public String getBAB_Flow() {
        return BAB_Flow;
    }

    public void setBAB_Flow(String BAB_Flow) {
        this.BAB_Flow = BAB_Flow;
    }

    public String getTest_Flow() {
        return Test_Flow;
    }

    public void setTest_Flow(String Test_Flow) {
        this.Test_Flow = Test_Flow;
    }

    public String getPacking_Flow() {
        return Packing_Flow;
    }

    public void setPacking_Flow(String Packing_Flow) {
        this.Packing_Flow = Packing_Flow;
    }

    public String getPart_Link() {
        return Part_Link;
    }

    public void setPart_Link(String Part_Link) {
        this.Part_Link = Part_Link;
    }

    public String getCE() {
        return CE;
    }

    public void setCE(String CE) {
        this.CE = CE;
    }

    public String getUL() {
        return UL;
    }

    public void setUL(String UL) {
        this.UL = UL;
    }

    public String getROHS() {
        return ROHS;
    }

    public void setROHS(String ROHS) {
        this.ROHS = ROHS;
    }

    public String getWEEE() {
        return WEEE;
    }

    public void setWEEE(String WEEE) {
        this.WEEE = WEEE;
    }

    public String getMade_in_Taiwan() {
        return Made_in_Taiwan;
    }

    public void setMade_in_Taiwan(String Made_in_Taiwan) {
        this.Made_in_Taiwan = Made_in_Taiwan;
    }

    public String getFCC() {
        return FCC;
    }

    public void setFCC(String FCC) {
        this.FCC = FCC;
    }

    public String getEAC() {
        return EAC;
    }

    public void setEAC(String EAC) {
        this.EAC = EAC;
    }

    public String getN_in_1_collection_box() {
        return N_in_1_collection_box;
    }

    public void setN_in_1_collection_box(String N_in_1_collection_box) {
        this.N_in_1_collection_box = N_in_1_collection_box;
    }

    public String getPartNo_attr_maintain() {
        return PartNo_attr_maintain;
    }

    public void setPartNo_attr_maintain(String PartNo_attr_maintain) {
        this.PartNo_attr_maintain = PartNo_attr_maintain;
    }

    public Integer getASSY_stations() {
        return ASSY_stations;
    }

    public void setASSY_stations(Integer ASSY_stations) {
        this.ASSY_stations = ASSY_stations;
    }

    public Integer getPacking_stations() {
        return Packing_stations;
    }

    public void setPacking_stations(Integer Packing_stations) {
        this.Packing_stations = Packing_stations;
    }

    public BigDecimal getASSY_lead_time() {
        return ASSY_lead_time;
    }

    public void setASSY_lead_time(BigDecimal ASSY_lead_time) {
        this.ASSY_lead_time = ASSY_lead_time;
    }

    public BigDecimal getASSY_kanban_time() {
        return ASSY_kanban_time;
    }

    public void setASSY_kanban_time(BigDecimal ASSY_kanban_time) {
        this.ASSY_kanban_time = ASSY_kanban_time;
    }

    public BigDecimal getPacking_lead_time() {
        return Packing_lead_time;
    }

    public void setPacking_lead_time(BigDecimal Packing_lead_time) {
        this.Packing_lead_time = Packing_lead_time;
    }

    public BigDecimal getPacking_kanban_time() {
        return Packing_kanban_time;
    }

    public void setPacking_kanban_time(BigDecimal Packing_kanban_time) {
        this.Packing_kanban_time = Packing_kanban_time;
    }

    public BigDecimal getCleanPanel_and_Assembly() {
        return CleanPanel_and_Assembly;
    }

    public void setCleanPanel_and_Assembly(BigDecimal CleanPanel_and_Assembly) {
        this.CleanPanel_and_Assembly = CleanPanel_and_Assembly;
    }

    public Date getModified_Date() {
        return Modified_Date;
    }

    public void setModified_Date(Date Modified_Date) {
        this.Modified_Date = Modified_Date;
    }

}
