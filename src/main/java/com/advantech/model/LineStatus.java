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
public enum LineStatus {
    OPEN(1),
    CLOSE(0);
 
    private final Integer value;
 
    private LineStatus(Integer value) {
        this.value = value;
    }
 
    public Integer getState() {
        return this.value;
    }
}
