/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class ErrorCode {

    private int id;
    private String name;
    private List<Integer> actionCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getActionCode() {
        return actionCode;
    }

    public void setActionCode(List<Integer> actionCode) {
        this.actionCode = actionCode;
    }

}
