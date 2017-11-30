/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "FBN")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Fbn implements Serializable {

    private int id;
    private String tagName;
    private String logDate;
    private String logTime;
    private int logMilliSecond;
    private int logValue;
    private Integer group;
    private Integer isused;
    private Integer diff;
    private Bab bab;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "TagName", length = 50, nullable = false)
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Column(name = "LogDate", length = 12, nullable = false)
    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    @Column(name = "LogTime", length = 12, nullable = false)
    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @Column(name = "LogMilliSecond", nullable = false)
    public int getLogMilliSecond() {
        return logMilliSecond;
    }

    public void setLogMilliSecond(int logMilliSecond) {
        this.logMilliSecond = logMilliSecond;
    }

    @Column(name = "LogValue", nullable = false)
    public int getLogValue() {
        return logValue;
    }

    public void setLogValue(int logValue) {
        this.logValue = logValue;
    }

    @Column(name = "groupid")
    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    @Column(name = "isused")
    public Integer getIsused() {
        return isused;
    }

    public void setIsused(Integer isused) {
        this.isused = isused;
    }

    @Column(name = "diff")
    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bab_id")
    public Bab getBab() {
        return bab;
    }

    public void setBab(Bab bab) {
        this.bab = bab;
    }

}
