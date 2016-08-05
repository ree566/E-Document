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
public class Countermeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int BABid;
    private int errorCode_id;
    private String reason;
    private String solution;
    private String editor;
    private String editTime;

    public Countermeasure() {
    }

    public Countermeasure(int BABid, String reason, String solution, String editor) {
        this.BABid = BABid;
        this.reason = reason;
        this.solution = solution;
        this.editor = editor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBABid() {
        return BABid;
    }

    public void setBABid(int BABid) {
        this.BABid = BABid;
    }

    public int getErrorCode_id() {
        return errorCode_id;
    }

    public void setErrorCode_id(int errorCode_id) {
        this.errorCode_id = errorCode_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

}
