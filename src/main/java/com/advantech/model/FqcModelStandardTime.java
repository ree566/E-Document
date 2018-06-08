/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "FqcModelStandardTime")
public class FqcModelStandardTime implements Serializable {

    private int id;
    private String modelNameCategory;
    private int standardTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "modelName_category", nullable = false, length = 50, unique = true)
    public String getModelNameCategory() {
        return modelNameCategory;
    }

    public void setModelNameCategory(String modelNameCategory) {
        this.modelNameCategory = modelNameCategory;
    }

    @Column(name = "standardTime", nullable = false)
    public int getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(int standardTime) {
        this.standardTime = standardTime;
    }
    
    
}
