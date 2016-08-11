/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;
import org.json.JSONArray;

/**
 *
 * @author Wei.Cheng
 */
//@Entity
public class BAB implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String PO;
    private String Model_name;
    private Integer line;
    private Integer people;
    private Integer isused;
    private String name;
    private String lineName;
    private String linetype;
    private String btime;
    private Integer cm_id; // check countermeasure is exist or not

    private boolean isBabClosed;

    //for saving line balance data, not exist in the database
    private JSONArray babavgs;

    public BAB() {

    }

    public BAB(String PO, String Model_name, Integer line, Integer people) {
        this.PO = PO;
        this.Model_name = Model_name;
        this.line = line;
        this.people = people;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String Model_name) {
        this.Model_name = Model_name;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Integer getIsused() {
        return isused;
    }

    public void setIsused(Integer isused) {
        this.isused = isused;
    }

    public String getName() {
        return name;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinetype() {
        return linetype;
    }

    public void setLinetype(String linetype) {
        this.linetype = linetype;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public JSONArray getBabavgs() {
        return babavgs;
    }

    public void setBabavgs(JSONArray babavgs) {
        this.babavgs = babavgs;
    }

    public Integer getCm_id() {
        return cm_id;
    }

    public void setCm_id(Integer cm_id) {
        this.cm_id = cm_id;
    }

    public boolean isIsBabClosed() {
        return this.isused != null;
    }

}

