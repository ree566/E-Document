/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class StandardWorkTime implements Serializable {

    @XmlElement(name = "UNIT_NO", required = true, nillable = true)
    protected String unitno;
    @XmlElement(name = "STATION_ID", required = true, nillable = true)
    protected Integer stationid;
    @XmlElement(name = "LINE_ID", required = true, nillable = true)
    protected Integer lineid;
    @XmlElement(name = "ITEM_NO", required = true, nillable = true)
    protected String itemno;
    @XmlElement(name = "TOTAL_CT", required = true, nillable = true)
    protected BigDecimal totalct;
    @XmlElement(name = "FIRST_TIME", required = true, nillable = true)
    protected BigDecimal firsttime;
    @XmlElement(name = "CT", required = true, nillable = true)
    protected BigDecimal ct;
    @XmlElement(name = "SIDE", required = true, nillable = true)
    protected Integer side;
    @XmlElement(name = "OP_CNT", required = true, nillable = true)
    protected Integer opcnt;
    @XmlElement(name = "KP_TYPE", required = true, nillable = true)
    protected String kptype;
    @XmlElement(name = "MACHINE_CNT", required = true, nillable = true)
    protected Integer machinecnt;
    @XmlElement(name = "MIX_CT", required = true, nillable = true)
    protected BigDecimal mixct;
    @XmlElement(name = "AUTO_CT", required = true, nillable = true)
    protected BigDecimal autoct;

    /**
     * 取得 unitno 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUNITNO() {
        return unitno;
    }

    /**
     * 設定 unitno 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUNITNO(String value) {
        this.unitno = value;
    }

    /**
     * 取得 stationid 特性的值.
     *
     */
    public Integer getSTATIONID() {
        return stationid;
    }

    /**
     * 設定 stationid 特性的值.
     *
     */
    public void setSTATIONID(Integer value) {
        this.stationid = value;
    }

    /**
     * 取得 lineid 特性的值.
     *
     */
    public Integer getLINEID() {
        return lineid;
    }

    /**
     * 設定 lineid 特性的值.
     *
     */
    public void setLINEID(Integer value) {
        this.lineid = value;
    }

    /**
     * 取得 itemno 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getITEMNO() {
        return itemno;
    }

    /**
     * 設定 itemno 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setITEMNO(String value) {
        this.itemno = value;
    }

    /**
     * 取得 totalct 特性的值.
     *
     * @return possible object is {@link BigDecimal }
     *
     */
    public BigDecimal getTOTALCT() {
        return totalct;
    }

    /**
     * 設定 totalct 特性的值.
     *
     * @param value allowed object is {@link BigDecimal }
     *
     */
    public void setTOTALCT(BigDecimal value) {
        this.totalct = value;
    }

    /**
     * 取得 firsttime 特性的值.
     *
     */
    public BigDecimal getFIRSTTIME() {
        return firsttime;
    }

    /**
     * 設定 firsttime 特性的值.
     *
     */
    public void setFIRSTTIME(BigDecimal value) {
        this.firsttime = value;
    }

    /**
     * 取得 ct 特性的值.
     *
     */
    public BigDecimal getCT() {
        return ct;
    }

    /**
     * 設定 ct 特性的值.
     *
     */
    public void setCT(BigDecimal value) {
        this.ct = value;
    }

    /**
     * 取得 side 特性的值.
     *
     */
    public Integer getSIDE() {
        return side;
    }

    /**
     * 設定 side 特性的值.
     *
     */
    public void setSIDE(Integer value) {
        this.side = value;
    }

    /**
     * 取得 opcnt 特性的值.
     *
     */
    public Integer getOPCNT() {
        return opcnt;
    }

    /**
     * 設定 opcnt 特性的值.
     *
     */
    public void setOPCNT(Integer value) {
        this.opcnt = value;
    }

    /**
     * 取得 kptype 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getKPTYPE() {
        return kptype;
    }

    /**
     * 設定 kptype 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setKPTYPE(String value) {
        this.kptype = value;
    }

    /**
     * 取得 machinecnt 特性的值.
     *
     */
    public Integer getMACHINECNT() {
        return machinecnt;
    }

    /**
     * 設定 machinecnt 特性的值.
     *
     */
    public void setMACHINECNT(Integer value) {
        this.machinecnt = value;
    }

    /**
     * 取得 mixct 特性的值.
     *
     */
    public BigDecimal getMIXCT() {
        return mixct;
    }

    /**
     * 設定 mixct 特性的值.
     *
     */
    public void setMIXCT(BigDecimal value) {
        this.mixct = value;
    }

    /**
     * 取得 autoct 特性的值.
     *
     */
    public BigDecimal getAUTOCT() {
        return autoct;
    }

    /**
     * 設定 autoct 特性的值.
     *
     */
    public void setAUTOCT(BigDecimal value) {
        this.autoct = value;
    }

}
