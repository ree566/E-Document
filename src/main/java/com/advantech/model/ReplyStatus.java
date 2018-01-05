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
public enum ReplyStatus {
    
    REPLIED(1),
    UNREPLIED(-1),
    NO_NEED_TO_REPLY(0);
 
    private final Integer status;
 
    private ReplyStatus(Integer status) {
        this.status = status;
    }
 
    public Integer getStatus() {
        return this.status;
    }
}
