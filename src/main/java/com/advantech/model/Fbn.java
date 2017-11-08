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
public class Fbn implements Serializable {

    private static final long serialVersionUID = 1L;

    private String TagName;
    private String LogDate;
    private String LogTime;
    private String LogMilliSecond;
    private int LogValue;
    private int groupid;
    private int id;
    private int isused;
    private int diff;
    private int BABid;

    public Fbn() {

    }

    public Fbn(String TagName, String LogDate, String LogTime, String LogMilliSecond, int LogValue, int groupid, int id, int isused, int diff) {
        this.TagName = TagName;
        this.LogDate = LogDate;
        this.LogTime = LogTime;
        this.LogMilliSecond = LogMilliSecond;
        this.LogValue = LogValue;
        this.groupid = groupid;
        this.id = id;
        this.isused = isused;
        this.diff = diff;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public String getLogDate() {
        return LogDate;
    }

    public void setLogDate(String LogDate) {
        this.LogDate = LogDate;
    }

    public String getLogTime() {
        return LogTime;
    }

    public void setLogTime(String LogTime) {
        this.LogTime = LogTime;
    }

    public String getLogMilliSecond() {
        return LogMilliSecond;
    }

    public void setLogMilliSecond(String LogMilliSecond) {
        this.LogMilliSecond = LogMilliSecond;
    }

    public int getLogValue() {
        return LogValue;
    }

    public void setLogValue(int LogValue) {
        this.LogValue = LogValue;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsused() {
        return isused;
    }

    public void setIsused(int isused) {
        this.isused = isused;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getBABid() {
        return BABid;
    }

    public void setBABid(int BABid) {
        this.BABid = BABid;
    }

    
}
