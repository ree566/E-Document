/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.security;

import com.advantech.converter.Encodeable;

/**
 *
 * @author Wei.Cheng
 */
public enum State implements Encodeable {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted"),
    LOCKED("Locked");

    private final String value;

    private State(final String value) {
        this.value = value;
    }

    @Override
    public String token() {
        return this.value;
    }
}
