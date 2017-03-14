package com.advantech.entity;
// Generated 2017/3/13 上午 09:50:39 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Type generated by hbm2java
 */

public class Type implements java.io.Serializable {

    private int id;

    private String name;

    @JsonManagedReference
    private Set sheetSpes = new HashSet(0);

    public Type() {
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type(int id, String name, Set sheetSpes) {
        this.id = id;
        this.name = name;
        this.sheetSpes = sheetSpes;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getSheetSpes() {
        return this.sheetSpes;
    }

    public void setSheetSpes(Set sheetSpes) {
        this.sheetSpes = sheetSpes;
    }

}
