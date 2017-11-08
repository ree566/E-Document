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
public class BabHistory implements Serializable {

    private int ID;
    private int T_Num;
    private String PO;
    private double average;
    private int BABid;
    private String TagName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getT_Num() {
        return T_Num;
    }

    public void setT_Num(int T_Num) {
        this.T_Num = T_Num;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getBABid() {
        return BABid;
    }

    public void setBABid(int BABid) {
        this.BABid = BABid;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

}
