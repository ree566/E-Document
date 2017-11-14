/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "LS_Table",
        schema = "dbo",
        catalog = "WebAccess"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestTable implements Serializable {

    private int id;
    private String name;
    private int sitefloor;

    @JsonIgnore
    private Set<Test> tests = new HashSet<Test>(0);

    @JsonIgnore
    private Set<TestRecord> testLineTypeRecords = new HashSet<TestRecord>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sitefloor", nullable = false)
    public int getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(int sitefloor) {
        this.sitefloor = sitefloor;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testTable")
    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testTable")
    public Set<TestRecord> getTestLineTypeRecords() {
        return testLineTypeRecords;
    }

    public void setTestLineTypeRecords(Set<TestRecord> testLineTypeRecords) {
        this.testLineTypeRecords = testLineTypeRecords;
    }

}
