/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "ModelSopRemark")
public class ModelSopRemark implements Serializable {

    private int id;
    private String modelName;
    private String remark;

    @JsonIgnore
    private Set<Line> lines = new HashSet<Line>(0);

    @JsonIgnore
    private Set<ModelSopRemarkDetail> modelSopRemarkDetails = new HashSet<ModelSopRemarkDetail>(0);

    @JsonIgnore
    private Set<ModelSopRemarkEvent> modelSopRemarkEvents = new HashSet<ModelSopRemarkEvent>(0);

    public ModelSopRemark() {
    }

    public ModelSopRemark(String modelName, String remark) {
        this.modelName = modelName;
        this.remark = remark;
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

    @Column(name = "modelName", nullable = false, length = 50)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ModelSopRemark_Line_REF", joinColumns = {
        @JoinColumn(name = "modelSopRemark_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "line_id", nullable = false, insertable = false, updatable = false)})
    public Set<Line> getLines() {
        return lines;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelSopRemark", orphanRemoval = true)
    public Set<ModelSopRemarkDetail> getModelSopRemarkDetails() {
        return modelSopRemarkDetails;
    }

    public void setModelSopRemarkDetails(Set<ModelSopRemarkDetail> modelSopRemarkDetails) {
        this.modelSopRemarkDetails = modelSopRemarkDetails;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelSopRemark", orphanRemoval = true)
    public Set<ModelSopRemarkEvent> getModelSopRemarkEvents() {
        return modelSopRemarkEvents;
    }

    public void setModelSopRemarkEvents(Set<ModelSopRemarkEvent> modelSopRemarkEvents) {
        this.modelSopRemarkEvents = modelSopRemarkEvents;
    }

}
