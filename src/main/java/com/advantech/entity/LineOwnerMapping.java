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
public class LineOwnerMapping implements Serializable {

    private int id;
    private Integer line_id;
    private String user_jobnumber;
    private String user_name;
    private String user_name_cn;
    private String email;
    private Integer abnormal_unfill_alarm;
    private Integer sensor_alarm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getLine_id() {
        return line_id;
    }

    public void setLine_id(Integer line_id) {
        this.line_id = line_id;
    }

    public String getUser_jobnumber() {
        return user_jobnumber;
    }

    public void setUser_jobnumber(String user_jobnumber) {
        this.user_jobnumber = user_jobnumber;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name_cn() {
        return user_name_cn;
    }

    public void setUser_name_cn(String user_name_cn) {
        this.user_name_cn = user_name_cn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAbnormalUnfillAlarm() {
        return abnormal_unfill_alarm != null && abnormal_unfill_alarm == 1;
    }

    public void setAbnormal_unfill_alarm(Integer abnormal_unfill_alarm) {
        this.abnormal_unfill_alarm = abnormal_unfill_alarm;
    }

    public boolean isSensorAlarm() {
        return sensor_alarm != null && sensor_alarm == 1;
    }

    public void setSensor_alarm(Integer sensor_alarm) {
        this.sensor_alarm = sensor_alarm;
    }

}
