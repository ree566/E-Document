/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.converter.Encodeable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public enum AttendanceStatus implements Encodeable {
    ON(1),
    OFF(0);

    private final Integer value;
    private static final Map map = new HashMap<>();

    private AttendanceStatus(Integer value) {
        this.value = value;
    }

    static {
        for (AttendanceStatus b : AttendanceStatus.values()) {
            map.put(b.value, b);
        }
    }

    public static AttendanceStatus valueOf(int pageType) {
        return (AttendanceStatus) map.get(pageType);
    }

    @Override
    public Integer token() {
        return this.value;
    }
}
