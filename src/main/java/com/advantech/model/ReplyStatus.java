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
public enum ReplyStatus implements Encodeable {

    REPLIED(1),
    UNREPLIED(-1),
    NO_NEED_TO_REPLY(0);

    private final Integer e_token;

    private ReplyStatus(Integer status) {
        this.e_token = status;
    }

    @Override
    public Integer token() {
        return this.e_token;
    }
}
