/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
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
@Table(name = "_BwField",
        schema = "dbo",
        catalog = "E_Document"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BwField implements java.io.Serializable {

    private int id;
    private BigDecimal assyAvg;
    private BigDecimal packingAvg;

    private Worktime worktime;

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
    @JoinColumn(name = "worktime_id", nullable = false, insertable = false, updatable = false)
    public Worktime getWorktime() {
        return this.worktime;
    }

    public void setWorktime(Worktime worktime) {
        this.worktime = worktime;
    }

    @Column(name = "assy_avg")
    public BigDecimal getAssyAvg() {
        return assyAvg;
    }

    public void setAssyAvg(BigDecimal assyAvg) {
        this.assyAvg = assyAvg;
    }

    @Column(name = "packing_avg")
    public BigDecimal getPackingAvg() {
        return packingAvg;
    }

    public void setPackingAvg(BigDecimal packingAvg) {
        this.packingAvg = packingAvg;
    }

}
