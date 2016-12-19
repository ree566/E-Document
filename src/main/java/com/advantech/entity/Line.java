/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 */
//@Entity
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private int isused;
    private String linetype;
    private int people;
    private int lock;

    private boolean isOpened;

    public Line() {

    }

    public Line(int id, String name, int isused, String linetype, int lock) {
        this.id = id;
        this.name = name;
        this.isused = isused;
        this.linetype = linetype;
        this.lock = lock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsused() {
        return isused;
    }

    public void setIsused(int isused) {
        this.isused = isused;
    }

    public String getLinetype() {
        return linetype;
    }

    public void setLinetype(String linetype) {
        this.linetype = linetype;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public boolean isOpened() {
        return this.isused == 1;
    }

}
