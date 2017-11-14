/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Countermeasure",
        schema = "dbo",
        catalog = "WebAccess"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Countermeasure implements Serializable {

    private int id;
    private int BABid;
    private String solution;
    private String lastEditor;
    private int lock;

    private Set<ErrorCode> errorCodes = new HashSet<ErrorCode>(0);

    private Set<ActionCode> actionCodes = new HashSet<ActionCode>(0);

    private Set<CountermeasureEvent> countermeasureEvents = new HashSet<CountermeasureEvent>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "BABid", nullable = false)
    public int getBABid() {
        return BABid;
    }

    public void setBABid(int BABid) {
        this.BABid = BABid;
    }

    @Column(name = "solution", nullable = false, length = 500)
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Column(name = "lastEditor", nullable = false, length = 50)
    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    @Column(name = "lock", nullable = false)
    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Countermeasure_ErrorCode_REF", schema = "dbo", catalog = "WebAccess", joinColumns = {
        @JoinColumn(name = "cm_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ec_id", nullable = false, insertable = false, updatable = false)})
    public Set<ErrorCode> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<ErrorCode> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Countermeasure_ActionCode_REF", schema = "dbo", catalog = "WebAccess", joinColumns = {
        @JoinColumn(name = "cm_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ac_id", nullable = false, insertable = false, updatable = false)})
    public Set<ActionCode> getActionCodes() {
        return actionCodes;
    }

    public void setActionCodes(Set<ActionCode> actionCodes) {
        this.actionCodes = actionCodes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "countermeasure")
    public Set<CountermeasureEvent> getCountermeasureEvents() {
        return countermeasureEvents;
    }

    public void setCountermeasureEvents(Set<CountermeasureEvent> countermeasureEvents) {
        this.countermeasureEvents = countermeasureEvents;
    }

}
