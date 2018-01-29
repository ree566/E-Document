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
@Table(name = "CountermeasureSopRecord")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CountermeasureSopRecord implements Serializable {

    private int id;
    private Countermeasure countermeasure;
    private String sop;

    public CountermeasureSopRecord() {
    }

    public CountermeasureSopRecord(Countermeasure countermeasure, String sop) {
        this.countermeasure = countermeasure;
        this.sop = sop;
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
    @JoinColumn(name = "cm_id", nullable = false)
    public Countermeasure getCountermeasure() {
        return countermeasure;
    }

    public void setCountermeasure(Countermeasure countermeasure) {
        this.countermeasure = countermeasure;
    }

    @Column(name = "sop", nullable = false, length = 200)
    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }

}
