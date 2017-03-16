package com.advantech.entity;
// Generated 2017/3/15 上午 09:14:05 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LabelInfo generated by hbm2java
 */
@Entity
@Table(name = "LabelInfo",
        schema = "dbo",
        catalog = "E_Document"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LabelInfo implements java.io.Serializable {

    private int id;
    private SheetSpe sheetSpe;
    private String ce;
    private String ul;
    private String rohs;
    private String weee;
    private String madeInTaiwan;
    private String fcc;
    private String eac;

    public LabelInfo() {
    }

    public LabelInfo(int id) {
        this.id = id;
    }

    public LabelInfo(int id, SheetSpe sheetSpe, String ce, String ul, String rohs, String weee, String madeInTaiwan, String fcc, String eac) {
        this.id = id;
        this.sheetSpe = sheetSpe;
        this.ce = ce;
        this.ul = ul;
        this.rohs = rohs;
        this.weee = weee;
        this.madeInTaiwan = madeInTaiwan;
        this.fcc = fcc;
        this.eac = eac;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sheet_spe_id")
    public SheetSpe getSheetSpe() {
        return this.sheetSpe;
    }

    public void setSheetSpe(SheetSpe sheetSpe) {
        this.sheetSpe = sheetSpe;
    }

    @Column(name = "CE", length = 50)
    public String getCe() {
        return this.ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    @Column(name = "UL", length = 50)
    public String getUl() {
        return this.ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    @Column(name = "ROHS", length = 50)
    public String getRohs() {
        return this.rohs;
    }

    public void setRohs(String rohs) {
        this.rohs = rohs;
    }

    @Column(name = "WEEE", length = 50)
    public String getWeee() {
        return this.weee;
    }

    public void setWeee(String weee) {
        this.weee = weee;
    }

    @Column(name = "Made_in_Taiwan", length = 50)
    public String getMadeInTaiwan() {
        return this.madeInTaiwan;
    }

    public void setMadeInTaiwan(String madeInTaiwan) {
        this.madeInTaiwan = madeInTaiwan;
    }

    @Column(name = "FCC", length = 50)
    public String getFcc() {
        return this.fcc;
    }

    public void setFcc(String fcc) {
        this.fcc = fcc;
    }

    @Column(name = "EAC", length = 50)
    public String getEac() {
        return this.eac;
    }

    public void setEac(String eac) {
        this.eac = eac;
    }

}
