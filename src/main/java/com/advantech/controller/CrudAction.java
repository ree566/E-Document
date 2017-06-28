/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

/**
 *
 * @author Wei.Cheng
 */
public enum CrudAction {
    ADD("add"),
    EDIT("edit"),
    DELETE("del");

    private final String action;

    private CrudAction(final String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return this.action;
    }
}
