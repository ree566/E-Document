/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

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
@Table(name = "FqcTime_Temp")
public class FqcTimeTemp implements Serializable {

    private int id;
    private Fqc fqc;
    private int timePeriod;

    public FqcTimeTemp() {
    }

    public FqcTimeTemp(Fqc fqc, int timePeriod) {
        this.fqc = fqc;
        this.timePeriod = timePeriod;
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

    @Column(name = "timePeriod", nullable = false)
    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

}
