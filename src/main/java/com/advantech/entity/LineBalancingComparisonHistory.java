/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
//@Entity
public class LineBalancingComparisonHistory implements Serializable {

    private static final long serialVersionUID = 1L;

     private String errorReason;
    private int id;

    public LineBalancingComparisonHistory(int id, String errorReason) {
        this.id = id;
        this.errorReason = errorReason;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
