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
@Table(name = "BabPreAssyPcsRecord")
public class BabPreAssyPcsRecord implements Serializable {

    private int id;
    private BabSettingHistory babSettingHistory;
    private int pcs;

    public BabPreAssyPcsRecord() {
    }

    public BabPreAssyPcsRecord(BabSettingHistory babSettingHistory, int pcs) {
        this.babSettingHistory = babSettingHistory;
        this.pcs = pcs;
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
    @JoinColumn(name = "babSettingHistory_id", nullable = false)
    public BabSettingHistory getBabSettingHistory() {
        return babSettingHistory;
    }

    public void setBabSettingHistory(BabSettingHistory babSettingHistory) {
        this.babSettingHistory = babSettingHistory;
    }

    @Column(name = "pcs", nullable = false)
    public int getPcs() {
        return pcs;
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

}
