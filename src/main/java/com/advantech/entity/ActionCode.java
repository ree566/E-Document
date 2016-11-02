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
public class ActionCode implements Serializable{

    private int id;
    private String name;
    private int ec_id;

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

    public int getEc_id() {
        return ec_id;
    }

    public void setEc_id(int ec_id) {
        this.ec_id = ec_id;
    }

}
