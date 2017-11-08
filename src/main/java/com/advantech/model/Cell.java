/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
//@Entity
public class Cell implements Serializable {

    private int id;
    private int lineId;
    private String type;
    private String PO;
    private String Model_name;
    private Integer isused;
    private String btime;

    public Cell() {
    }

    public Cell(int lineId, String type, String PO, String Model_name) {
        this.lineId = lineId;
        this.type = type;
        this.PO = PO;
        this.Model_name = Model_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String Model_name) {
        this.Model_name = Model_name;
    }

    public Integer getIsused() {
        return isused;
    }

    public void setIsused(Integer isused) {
        this.isused = isused;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public boolean isClosed() {
        return this.isused != null;
    }
}
