/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Temp_Mes_PassCnt")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RPT405")
public class MesPassCountRecord implements Serializable {

    private int id;

    @XmlElement(name = "LINE_ID")
    private int mesLineId;

    @XmlElement(name = "WIP_NO")
    private String po;

    @XmlElement(name = "OUTPUT_QTY")
    private int outputQuanitiy;

    @XmlElement(name = "CREATE_DATE")
    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mes_line_id", nullable = false)
    public int getMesLineId() {
        return mesLineId;
    }

    public void setMesLineId(int mesLineId) {
        this.mesLineId = mesLineId;
    }

    @Column(name = "po", nullable = false)
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @Column(name = "output_qty", nullable = false)
    public int getOutputQuanitiy() {
        return outputQuanitiy;
    }

    public void setOutputQuanitiy(int outputQuanitiy) {
        this.outputQuanitiy = outputQuanitiy;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 23)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.mesLineId;
        hash = 53 * hash + Objects.hashCode(this.po);
        hash = 53 * hash + Objects.hashCode(this.createDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MesPassCountRecord other = (MesPassCountRecord) obj;
        if (this.mesLineId != other.mesLineId) {
            return false;
        }
        if (!Objects.equals(this.po, other.po)) {
            return false;
        }
        if (!Objects.equals(this.createDate, other.createDate)) {
            return false;
        }
        return true;
    }

}
