/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "FqcLoginRecord")
public class FqcLoginRecord implements Serializable {

    private int id;
    private FqcLine fqcLine;
    private String jobnumber;

    public FqcLoginRecord() {
    }

    public FqcLoginRecord(FqcLine fqcLine, String jobnumber) {
        this.fqcLine = fqcLine;
        this.jobnumber = jobnumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fqcLine_id", nullable = false)
    public FqcLine getFqcLine() {
        return fqcLine;
    }

    public void setFqcLine(FqcLine fqcLine) {
        this.fqcLine = fqcLine;
    }

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "jobnumber", length = 50, nullable = false)
    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

}
