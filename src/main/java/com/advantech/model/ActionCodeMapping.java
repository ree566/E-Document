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
public class ActionCodeMapping implements Serializable {

    private int id;
    private int ac_id;
    private String manager;
    private String user_name;
    private String sitefloor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAc_id() {
        return ac_id;
    }

    public void setAc_id(int ac_id) {
        this.ac_id = ac_id;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(String sitefloor) {
        this.sitefloor = sitefloor;
    }
    
}
