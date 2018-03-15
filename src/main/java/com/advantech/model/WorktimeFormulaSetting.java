package com.advantech.model;
// Generated 2017/5/31 下午 04:33:40 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * WorktimeFormulaSetting generated by hbm2java
 */
@Entity
@Table(name = "Worktime_FormulaSetting"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorktimeFormulaSetting implements java.io.Serializable {

    private int id;
    private Worktime worktime;
    private int productionWt = 1;
    private int setupTime = 1;
    private int assyToT1 = 1;
    private int t2ToPacking = 1;
    private int assyLeadTime = 1;
    private int test = 1;

    public WorktimeFormulaSetting() {
    }

    public WorktimeFormulaSetting(Worktime worktime) {
        this.worktime = worktime;
    }

    public WorktimeFormulaSetting(int id, Worktime worktime, int productionWt, 
            int setupTime, int assyToT1, int t2ToPacking, int assyLeadTime, int test) {
        this.id = id;
        this.worktime = worktime;
        this.productionWt = productionWt;
        this.setupTime = setupTime;
        this.assyToT1 = assyToT1;
        this.t2ToPacking = t2ToPacking;
        this.assyLeadTime = assyLeadTime;
        this.test = test;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worktime_id", nullable = true)
    public Worktime getWorktime() {
        return this.worktime;
    }

    public void setWorktime(Worktime worktime) {
        this.worktime = worktime;
    }

    @Column(name = "productionWt", nullable = false)
    public int getProductionWt() {
        return this.productionWt;
    }

    public void setProductionWt(int productionWt) {
        this.productionWt = productionWt;
    }

    @Column(name = "setup_time", nullable = false)
    public int getSetupTime() {
        return this.setupTime;
    }

    public void setSetupTime(int setupTime) {
        this.setupTime = setupTime;
    }

    @Column(name = "assy_to_t1", nullable = false)
    public int getAssyToT1() {
        return this.assyToT1;
    }

    public void setAssyToT1(int assyToT1) {
        this.assyToT1 = assyToT1;
    }

    @Column(name = "t2_to_packing", nullable = false)
    public int getT2ToPacking() {
        return this.t2ToPacking;
    }

    public void setT2ToPacking(int t2ToPacking) {
        this.t2ToPacking = t2ToPacking;
    }

    @Column(name = "assy_lead_time", nullable = false)
    public int getAssyLeadTime() {
        return assyLeadTime;
    }

    public void setAssyLeadTime(int assyLeadTime) {
        this.assyLeadTime = assyLeadTime;
    }

    @Column(name = "test", nullable = false)
    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

}
