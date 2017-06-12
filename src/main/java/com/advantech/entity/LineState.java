/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

/**
 *
 * @author Wei.Cheng
 */
public enum LineState {
    OPEN(1),
    CLOSE(0);
 
    private final Integer value;
 
    private LineState(Integer value) {
        this.value = value;
    }
 
    public Integer getState() {
        return this.value;
    }
}
