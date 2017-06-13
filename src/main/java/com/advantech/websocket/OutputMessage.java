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
public class OutputMessage {

    private String username;
    private Integer rowId;
    private String action;
    private String time;

    public OutputMessage(String username, Integer rowId, String action, String time) {
        this.username = username;
        this.rowId = rowId;
        this.action = action;
        this.time = time;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
