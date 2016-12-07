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
public class CellLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int aps_lineId;
    private int sitefloor;
    private int isused;
    private String lastUpdateTime;

    public CellLine() {

    }

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

    public int getAps_lineId() {
        return aps_lineId;
    }

    public void setAps_lineId(int aps_lineId) {
        this.aps_lineId = aps_lineId;
    }

    public int getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(int sitefloor) {
        this.sitefloor = sitefloor;
    }

    public int getIsused() {
        return isused;
    }

    public void setIsused(int isused) {
        this.isused = isused;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public boolean isOpened(){
        return this.isused == 1;
    }

}
