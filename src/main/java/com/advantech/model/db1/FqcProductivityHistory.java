/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import java.io.Serializable;
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
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "FqcProducitvityHistory")
public class FqcProductivityHistory implements Serializable {

    private int id;
    private Fqc fqc;
    private int pcs;
    private int timeCost;
    private int standardTime;

    public FqcProductivityHistory() {
    }

    public FqcProductivityHistory(Fqc fqc, int pcs, int timeCost, int standardTime) {
        this.fqc = fqc;
        this.pcs = pcs;
        this.timeCost = timeCost;
        this.standardTime = standardTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fqc_id", nullable = false)
    public Fqc getFqc() {
        return fqc;
    }

    public void setFqc(Fqc fqc) {
        this.fqc = fqc;
    }

    @Column(name = "pcs", nullable = false)
    public int getPcs() {
        return pcs;
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    @Column(name = "time_cost", nullable = false)
    public int getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(int timeCost) {
        this.timeCost = timeCost;
    }

    @Column(name = "standardTime", nullable = false)
    public int getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(int standardTime) {
        this.standardTime = standardTime;
    }

}
