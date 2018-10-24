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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Sensor_Transform")
public class SensorTransform implements Serializable {

    private String name;

    @JsonIgnore
    private Set<TagNameComparison> tagNameComparisons = new HashSet<>(0);

    @JsonIgnore
    private Set<BabSettingHistory> babSettingHistorys = new HashSet<>(0);

    @JsonIgnore
    private Set<BabPcsDetailHistory> babPcsDetailHistorys = new HashSet<>(0);

    @JsonIgnore
    private Set<BabSensorLoginRecord> babSensorLoginRecords = new HashSet<>(0);

    @JsonIgnore
    private Set<BabPassStationRecord> babPassStationRecords = new HashSet<>(0);

    public SensorTransform() {
    }

    public SensorTransform(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.lampSysTagName", cascade = CascadeType.ALL)
    public Set<TagNameComparison> getTagNameComparisons() {
        return tagNameComparisons;
    }

    public void setTagNameComparisons(Set<TagNameComparison> tagNameComparisons) {
        this.tagNameComparisons = tagNameComparisons;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tagName")
    public Set<BabSettingHistory> getBabSettingHistorys() {
        return babSettingHistorys;
    }

    public void setBabSettingHistorys(Set<BabSettingHistory> babSettingHistorys) {
        this.babSettingHistorys = babSettingHistorys;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tagName")
    public Set<BabPcsDetailHistory> getBabPcsDetailHistorys() {
        return babPcsDetailHistorys;
    }

    public void setBabPcsDetailHistorys(Set<BabPcsDetailHistory> babPcsDetailHistorys) {
        this.babPcsDetailHistorys = babPcsDetailHistorys;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tagName")
    public Set<BabSensorLoginRecord> getBabSensorLoginRecords() {
        return babSensorLoginRecords;
    }

    public void setBabSensorLoginRecords(Set<BabSensorLoginRecord> babSensorLoginRecords) {
        this.babSensorLoginRecords = babSensorLoginRecords;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tagName")
    public Set<BabPassStationRecord> getBabPassStationRecords() {
        return babPassStationRecords;
    }

    public void setBabPassStationRecords(Set<BabPassStationRecord> babPassStationRecords) {
        this.babPassStationRecords = babPassStationRecords;
    }

}
