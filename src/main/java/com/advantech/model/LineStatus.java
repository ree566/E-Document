/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.converter.Encodeable;

/**
 *
 * @author Wei.Cheng
 */
public enum LineStatus implements Encodeable{
    
    /**
     * 1 for open sign
     */
    OPEN(1),
    
    /**
     * 0 for close sign
     */
    CLOSE(0);
 
    private final Integer e_token;
 
    private LineStatus(Integer e_token) {
        this.e_token = e_token;
    }
 
    @Override
    public Integer token() {
        return this.e_token;
    }
}
