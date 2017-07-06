/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "BwWorktimeAvg_view",
        schema = "dbo",
        catalog = "E_Document",
        uniqueConstraints = @UniqueConstraint(columnNames = "id")
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = BwAvgView.class)
public class BwAvgView implements java.io.Serializable {

    private int id;
    private String modelName;
    private BigDecimal assyAvg;
    private BigDecimal packingAvg;

    @JsonIgnore
    private Set<Worktime> worktimes = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "model_name")
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bwAvgView")
    public Set<Worktime> getWorktimes() {
        return worktimes;
    }

    public void setWorktimes(Set<Worktime> worktimes) {
        this.worktimes = worktimes;
    }

}
