/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
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
@Table(name = "BabAlarmHistory")
public class BabAlarmHistory implements Serializable {

    private int id;
    private Bab bab;
    private int failPcs;
    private int totalPcs;
    private BigDecimal balance;
    private int alarmPercentIsPass;
    private int balanceIsPass;

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
    @JoinColumn(name = "bab_id", nullable = false)
    public Bab getBab() {
        return bab;
    }

    public void setBab(Bab bab) {
        this.bab = bab;
    }

    @Column(name = "failPcs", nullable = false)
    public int getFailPcs() {
        return failPcs;
    }

    public void setFailPcs(int failPcs) {
        this.failPcs = failPcs;
    }

    @Column(name = "totalPcs", nullable = false)
    public int getTotalPcs() {
        return totalPcs;
    }

    public void setTotalPcs(int totalPcs) {
        this.totalPcs = totalPcs;
    }

    @Column(name = "balance", nullable = false)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Column(name = "alarmPercentIsPass", nullable = false)
    public int getAlarmPercentIsPass() {
        return alarmPercentIsPass;
    }

    public void setAlarmPercentIsPass(int alarmPercentIsPass) {
        this.alarmPercentIsPass = alarmPercentIsPass;
    }

    @Column(name = "balanceIsPass", nullable = false)
    public int getBalanceIsPass() {
        return balanceIsPass;
    }

    public void setBalanceIsPass(int balanceIsPass) {
        this.balanceIsPass = balanceIsPass;
    }

}
