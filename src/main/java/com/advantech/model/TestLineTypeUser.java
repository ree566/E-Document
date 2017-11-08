/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "QryData")
public class TestLineTypeUser implements Serializable {

//    @XmlElement(name = "C_DATE")
//    private String C_DATE;
    @XmlElement(name = "USER_NO")
    private String userNo;

    @XmlElement(name = "USER_NAME")
    private String userName;

//    @XmlElement(name = "TOTAL_PQ")
//    private String TOTAL_PQ;
//
//    @XmlElement(name = "TOTAL_FQ")
//    private String TOTAL_FQ;
//
//    @XmlElement(name = "TOTAL_CT")
//    private String TOTAL_CT;
//
//    @XmlElement(name = "PHOTO")
//    private String PHOTO;
//
//    @XmlElement(name = "START_TIME")
//    private String START_TIME;
//
//    @XmlElement(name = "END_TIME")
//    private String END_TIME;
//
//    @XmlElement(name = "INPUT_H")
//    private String INPUT_H;
//
//    @XmlElement(name = "BREAK_H")
//    private String BREAK_H;
    @XmlElement(name = "PRODUCTIVITY")
    private Double productivity;

//    public String getC_DATE() {
//        return C_DATE;
//    }
//
//    public void setC_DATE(String C_DATE) {
//        this.C_DATE = C_DATE;
//    }
    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getTOTAL_PQ() {
//        return TOTAL_PQ;
//    }
//
//    public void setTOTAL_PQ(String TOTAL_PQ) {
//        this.TOTAL_PQ = TOTAL_PQ;
//    }
//
//    public String getTOTAL_FQ() {
//        return TOTAL_FQ;
//    }
//
//    public void setTOTAL_FQ(String TOTAL_FQ) {
//        this.TOTAL_FQ = TOTAL_FQ;
//    }
//
//    public String getTOTAL_CT() {
//        return TOTAL_CT;
//    }
//
//    public void setTOTAL_CT(String TOTAL_CT) {
//        this.TOTAL_CT = TOTAL_CT;
//    }
//
//    public String getPHOTO() {
//        return PHOTO;
//    }
//
//    public void setPHOTO(String PHOTO) {
//        this.PHOTO = PHOTO;
//    }
//
//    public String getSTART_TIME() {
//        return START_TIME;
//    }
//
//    public void setSTART_TIME(String START_TIME) {
//        this.START_TIME = START_TIME;
//    }
//
//    public String getEND_TIME() {
//        return END_TIME;
//    }
//
//    public void setEND_TIME(String END_TIME) {
//        this.END_TIME = END_TIME;
//    }
//
//    public String getINPUT_H() {
//        return INPUT_H;
//    }
//
//    public void setINPUT_H(String INPUT_H) {
//        this.INPUT_H = INPUT_H;
//    }
//
//    public String getBREAK_H() {
//        return BREAK_H;
//    }
//
//    public void setBREAK_H(String BREAK_H) {
//        this.BREAK_H = BREAK_H;
//    }
    public Double getProductivity() {
        return productivity;
    }

    public void setProductivity(Double productivity) {
        this.productivity = productivity;
    }

}
