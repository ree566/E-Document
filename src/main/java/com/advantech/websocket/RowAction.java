/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

/**
 *
 * @author Wei.Cheng
 */
public enum RowAction {
    LOCK("LOCK"),
    UNLOCK("UNLOCK"),
    ERROR_MESSAGE("ERROR_MESSAGE");

    private final String rowAction;

    private RowAction(final String rowAction) {
        this.rowAction = rowAction;
    }

    @Override
    public String toString() {
        return this.rowAction;
    }
}
