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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Flow_Permutations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FlowPermutations implements Serializable {

    private int id;
    private String babFlow;
    private String testFlow;
    private String packingFlow;
    private String code;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "babFlow", length = 50)
    public String getBabFlow() {
        return babFlow;
    }

    public void setBabFlow(String babFlow) {
        this.babFlow = babFlow;
    }

    @Column(name = "testFlow", length = 50)
    public String getTestFlow() {
        return testFlow;
    }

    public void setTestFlow(String testFlow) {
        this.testFlow = testFlow;
    }

    @Column(name = "packingFlow", length = 50)
    public String getPackingFlow() {
        return packingFlow;
    }

    public void setPackingFlow(String packingFlow) {
        this.packingFlow = packingFlow;
    }

    @Column(name = "code", length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
