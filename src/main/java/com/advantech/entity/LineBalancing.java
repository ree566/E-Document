/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Wei.Cheng
 */
public class LineBalancing implements Serializable {

    private int id;
    private int Number_of_poople;
    private String Do_not_stop;
    private String PO;
    private String PN;
    private double Balance;
    private double Do_not1;
    private String SOP1;
    private double Do_not2;
    private String SOP2;
    private double Do_not3;
    private String SOP3;
    private double Do_not4;
    private String SOP4;
    private double Do_not5;
    private String SOP5;
    private double Do_not6;
    private String SOP6;
    private String Remark;
    private String Update_Name;
    private Date Update_time;
    private String Line;
    private Date Current_Save_Time;
    private String Editor_IP;

    public LineBalancing() {

    }

    public LineBalancing(int Number_of_poople, String Do_not_stop, String PO, String PN, double Balance, double Do_not1, double Do_not2, double Do_not3, double Do_not4, String Line) {
        this.Number_of_poople = Number_of_poople;
        this.Do_not_stop = Do_not_stop;
        this.PO = PO;
        this.PN = PN;
        this.Balance = Balance;
        this.Do_not1 = Do_not1;
        this.Do_not2 = Do_not2;
        this.Do_not3 = Do_not3;
        this.Do_not4 = Do_not4;
        this.Line = Line;
    }

    public LineBalancing(int id, String SOP1, String SOP2, String SOP3, String SOP4, String Remark, String Update_Name, String Editor_IP) {
        this.id = id;
        this.SOP1 = SOP1;
        this.SOP2 = SOP2;
        this.SOP3 = SOP3;
        this.SOP4 = SOP4;
        this.Remark = Remark;
        this.Update_Name = Update_Name;
        this.Editor_IP = Editor_IP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber_of_poople() {
        return Number_of_poople;
    }

    public void setNumber_of_poople(int Number_of_poople) {
        this.Number_of_poople = Number_of_poople;
    }

    public String getDo_not_stop() {
        return Do_not_stop;
    }

    public void setDo_not_stop(String Do_not_stop) {
        this.Do_not_stop = Do_not_stop;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getPN() {
        return PN;
    }

    public void setPN(String PN) {
        this.PN = PN;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getDo_not1() {
        return Do_not1;
    }

    public void setDo_not1(double Do_not1) {
        this.Do_not1 = Do_not1;
    }

    public String getSOP1() {
        return SOP1;
    }

    public void setSOP1(String SOP1) {
        this.SOP1 = SOP1;
    }

    public double getDo_not2() {
        return Do_not2;
    }

    public void setDo_not2(double Do_not2) {
        this.Do_not2 = Do_not2;
    }

    public String getSOP2() {
        return SOP2;
    }

    public void setSOP2(String SOP2) {
        this.SOP2 = SOP2;
    }

    public double getDo_not3() {
        return Do_not3;
    }

    public void setDo_not3(double Do_not3) {
        this.Do_not3 = Do_not3;
    }

    public String getSOP3() {
        return SOP3;
    }

    public void setSOP3(String SOP3) {
        this.SOP3 = SOP3;
    }

    public double getDo_not4() {
        return Do_not4;
    }

    public void setDo_not4(double Do_not4) {
        this.Do_not4 = Do_not4;
    }

    public String getSOP4() {
        return SOP4;
    }

    public void setSOP4(String SOP4) {
        this.SOP4 = SOP4;
    }

    public double getDo_not5() {
        return Do_not5;
    }

    public void setDo_not5(double Do_not5) {
        this.Do_not5 = Do_not5;
    }

    public String getSOP5() {
        return SOP5;
    }

    public void setSOP5(String SOP5) {
        this.SOP5 = SOP5;
    }

    public double getDo_not6() {
        return Do_not6;
    }

    public void setDo_not6(double Do_not6) {
        this.Do_not6 = Do_not6;
    }

    public String getSOP6() {
        return SOP6;
    }

    public void setSOP6(String SOP6) {
        this.SOP6 = SOP6;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getUpdate_Name() {
        return Update_Name;
    }

    public void setUpdate_Name(String Update_Name) {
        this.Update_Name = Update_Name;
    }

    public Date getUpdate_time() {
        return Update_time;
    }

    public void setUpdate_time(Date Update_time) {
        this.Update_time = Update_time;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String Line) {
        this.Line = Line;
    }

    public Date getCurrent_Save_Time() {
        return Current_Save_Time;
    }

    public void setCurrent_Save_Time(Date Current_Save_Time) {
        this.Current_Save_Time = Current_Save_Time;
    }

    public String getEditor_IP() {
        return Editor_IP;
    }

    public void setEditor_IP(String Editor_IP) {
        this.Editor_IP = Editor_IP;
    }

}
