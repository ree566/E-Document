/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Worktime_MaterialPropertyUpload_Setting",
        uniqueConstraints = @UniqueConstraint(columnNames = "mat_prop_no")
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorktimeMaterialPropertyUploadSetting implements java.io.Serializable {

    private int id;
    private String columnName;
    private String formula;
    private String affFormula;
    private String matPropNo;
    private String defaultValue;
    private String defaultAffValue;
    private int uploadWhenDefault;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "column_name", nullable = false, length = 50)
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Column(name = "formula", nullable = false, length = 500)
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Column(name = "aff_formula", length = 500)
    public String getAffFormula() {
        return affFormula;
    }

    public void setAffFormula(String affFormula) {
        this.affFormula = affFormula;
    }

    @Column(name = "mat_prop_no", length = 50)
    public String getMatPropNo() {
        return matPropNo;
    }

    public void setMatPropNo(String matPropNo) {
        this.matPropNo = matPropNo;
    }

    @Column(name = "default_value", length = 50)
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Transient
//    @Column(name = "default_aff_value", length = 50)
    public String getDefaultAffValue() {
        return defaultAffValue;
    }

    public void setDefaultAffValue(String defaultAffValue) {
        this.defaultAffValue = defaultAffValue;
    }

    @Column(name = "upload_when_default")
    public int getUploadWhenDefault() {
        return uploadWhenDefault;
    }

    public void setUploadWhenDefault(int uploadWhenDefault) {
        this.uploadWhenDefault = uploadWhenDefault;
    }

}
