/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Line_Balancing_Main")
@JsonIdentityInfo(generator = JSOGGenerator.class, property = "id")
public class LineBalancing implements Serializable {

    private int id;
    private int people;
    private String lineType;
    private String po;
    private String modelName;
    private Double balance;
    private Double avg1;
    private String sop1;
    private Double avg2;
    private String sop2;
    private Double avg3;
    private String sop3;
    private Double avg4;
    private String sop4;
    private Double avg5;
    private String sop5;
    private Double avg6;
    private String sop6;
    private String line_id;
    private Date lastUpdateTime;

    public LineBalancing() {
    }

    public LineBalancing(int people, String lineType, String po, String modelName, String line_id) {
        this.people = people;
        this.lineType = lineType;
        this.po = po;
        this.modelName = modelName;
        this.line_id = line_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Number_of_poople", nullable = false)
    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    @Column(name = "Do_not_stop", nullable = false, length = 20)
    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    @Column(name = "PO", nullable = false, length = 20)
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @Column(name = "PN", nullable = false, length = 20)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name = "Balance", precision = 10, scale = 4)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Column(name = "Do_not1", precision = 10, scale = 4)
    public Double getAvg1() {
        return avg1;
    }

    public void setAvg1(Double avg1) {
        this.avg1 = avg1;
    }

    @Column(name = "SOP1", length = 50)
    public String getSop1() {
        return sop1;
    }

    public void setSop1(String sop1) {
        this.sop1 = sop1;
    }

    @Column(name = "Do_not2", precision = 10, scale = 4)
    public Double getAvg2() {
        return avg2;
    }

    public void setAvg2(Double avg2) {
        this.avg2 = avg2;
    }

    @Column(name = "SOP2", length = 50)
    public String getSop2() {
        return sop2;
    }

    public void setSop2(String sop2) {
        this.sop2 = sop2;
    }

    @Column(name = "Do_not3", precision = 10, scale = 4)
    public Double getAvg3() {
        return avg3;
    }

    public void setAvg3(Double avg3) {
        this.avg3 = avg3;
    }

    @Column(name = "SOP3", length = 50)
    public String getSop3() {
        return sop3;
    }

    public void setSop3(String sop3) {
        this.sop3 = sop3;
    }

    @Column(name = "Do_not4", precision = 10, scale = 4)
    public Double getAvg4() {
        return avg4;
    }

    public void setAvg4(Double avg4) {
        this.avg4 = avg4;
    }

    @Column(name = "SOP4", length = 50)
    public String getSop4() {
        return sop4;
    }

    public void setSop4(String sop4) {
        this.sop4 = sop4;
    }

    @Column(name = "Do_not5", precision = 10, scale = 4)
    public Double getAvg5() {
        return avg5;
    }

    public void setAvg5(Double avg5) {
        this.avg5 = avg5;
    }

    @Column(name = "SOP5", length = 50)
    public String getSop5() {
        return sop5;
    }

    public void setSop5(String sop5) {
        this.sop5 = sop5;
    }

    @Column(name = "Do_not6", precision = 10, scale = 4)
    public Double getAvg6() {
        return avg6;
    }

    public void setAvg6(Double avg6) {
        this.avg6 = avg6;
    }

    @Column(name = "SOP6", length = 50)
    public String getSop6() {
        return sop6;
    }

    public void setSop6(String sop6) {
        this.sop6 = sop6;
    }

    @Column(name = "Line", nullable = false)
    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Current_Save_Time", length = 23, insertable = false, updatable = false)
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
