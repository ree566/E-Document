/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.converter.Encodeable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public enum CrudAction implements Encodeable{
    ADD("add"),
    EDIT("edit"),
    DEL("del");

    private final String value;
    private static final Map map = new HashMap<>();

    private CrudAction(final String value) {
        this.value = value;
    }
    
    static {
        for (CrudAction b : CrudAction.values()) {
            map.put(b.value, b);
        }
    }
    
    public static CrudAction valueOf(int pageType) {
        return (CrudAction) map.get(pageType);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public Object token() {
        return this.value;
    }
}
