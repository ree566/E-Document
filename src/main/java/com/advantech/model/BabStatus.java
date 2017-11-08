/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

/**
 *
 * @author Wei.Cheng
 */
public enum BabStatus {
    CLOSED(1),
    NO_RECORD(-1),
    UNFINSHED(-2);
 
    private final Integer value;
 
    private BabStatus(Integer value) {
        this.value = value;
    }
 
    public Integer getValue() {
        return this.value;
    }
}
