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
//@Entity
public class LineTypeConfig implements Serializable {

    private int id;
    private String linetype_name;
    private String variable_name;
    private Object variable_value;

    public LineTypeConfig() {
    }

    public LineTypeConfig(String linetype_name, String variable_name, Object variable_value) {
        this.linetype_name = linetype_name;
        this.variable_name = variable_name;
        this.variable_value = variable_value;
    }

    public LineTypeConfig(int id, String linetype_name, String variable_name, Object variable_value) {
        this.id = id;
        this.linetype_name = linetype_name;
        this.variable_name = variable_name;
        this.variable_value = variable_value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinetype_name() {
        return linetype_name;
    }

    public void setLinetype_name(String linetype_name) {
        this.linetype_name = linetype_name;
    }

    public String getVariable_name() {
        return variable_name;
    }

    public void setVariable_name(String variable_name) {
        this.variable_name = variable_name;
    }

    public Object getVariable_value() {
        return variable_value;
    }

    public void setVariable_value(Object variable_value) {
        this.variable_value = variable_value;
    }

}
