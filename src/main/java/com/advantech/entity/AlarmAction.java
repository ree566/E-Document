/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
public class AlarmAction implements Serializable {

    private String tableId;
    private int alarm;

    public AlarmAction() {
    }

    public AlarmAction(String tableId, int alarm) {
        this.tableId = tableId;
        this.alarm = alarm;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
