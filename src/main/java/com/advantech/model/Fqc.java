/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.converter.BabStatusConverter;
import com.advantech.converter.ReplyStatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Fqc")
public class Fqc implements Serializable {

    private int id;
    private String po;
    private String modelName;
    private FqcLine fqcLine;
    private BabStatus babStatus;
    private Date beginTime;
    private Date lastUpdateTime;
    private ReplyStatus replyStatus = ReplyStatus.NO_NEED_TO_REPLY;
    private String remark;

    @JsonIgnore
    private Set<FqcSettingHistory> fqcSettingHistorys = new HashSet<FqcSettingHistory>(0);

    @JsonIgnore
    private Set<FqcProducitvityHistory> fqcProducitvityHistorys = new HashSet<FqcProducitvityHistory>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "po", length = 50, nullable = false)
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "modelName", length = 50, nullable = false)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fqcLine_id", nullable = false)
    public FqcLine getFqcLine() {
        return fqcLine;
    }

    public void setFqcLine(FqcLine fqcLine) {
        this.fqcLine = fqcLine;
    }

    @Column(name = "isused")
    @Convert(converter = BabStatusConverter.class)
    public BabStatus getBabStatus() {
        return babStatus;
    }

    public void setBabStatus(BabStatus babStatus) {
        this.babStatus = babStatus;
    }

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "btime", length = 23)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdateTime", length = 23)
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @NotNull
    @Column(name = "replyFlag")
    @Convert(converter = ReplyStatusConverter.class)
    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(ReplyStatus replyStatus) {
        this.replyStatus = replyStatus;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fqc")
    public Set<FqcSettingHistory> getFqcSettingHistorys() {
        return fqcSettingHistorys;
    }

    public void setFqcSettingHistorys(Set<FqcSettingHistory> fqcSettingHistorys) {
        this.fqcSettingHistorys = fqcSettingHistorys;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fqc")
    public Set<FqcProducitvityHistory> getFqcProducitvityHistorys() {
        return fqcProducitvityHistorys;
    }

    public void setFqcProducitvityHistorys(Set<FqcProducitvityHistory> fqcProducitvityHistorys) {
        this.fqcProducitvityHistorys = fqcProducitvityHistorys;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
