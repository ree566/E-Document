/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;
import static java.lang.System.out;
import java.util.Objects;

/**
 *
 * @author Wei.Cheng
 */
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String tableName;
    private String userid;
    private String sitefloor;
    private String updatetime;

    public Test() {

    }

    public Test(int id, String userid) {
        this.id = id;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(String sitefloor) {
        this.sitefloor = sitefloor;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getTableNum() {
        return Integer.parseInt(this.tableName.replaceAll("\\D+", ""));
    }

}
