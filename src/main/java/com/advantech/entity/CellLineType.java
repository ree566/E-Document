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
public enum CellLineType {
    BAB("BAB"),
    PKG("PKG"),
    TEST("TEST");

    private final String state;

    private CellLineType(final String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }

}
