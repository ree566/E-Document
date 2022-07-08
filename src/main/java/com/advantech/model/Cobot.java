/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(
        name = "Cobot",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cobot implements java.io.Serializable {

    private int id;
    private String name;
    private BigDecimal worktimeMinutes;
    private BigDecimal worktimeSeconds;

    @JsonIgnore
    private Set<Worktime> worktimes = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "[name]", length = 50, unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "worktime_minutes", precision = 10, scale = 1)
    public BigDecimal getWorktimeMinutes() {
        return worktimeMinutes;
    }

    public void setWorktimeMinutes(BigDecimal worktimeMinutes) {
        this.worktimeMinutes = worktimeMinutes;
    }

    @Digits(integer = 10 /*precision*/, fraction = 1 /*scale*/)
    @Column(name = "worktime_seconds", precision = 10, scale = 1)
    public BigDecimal getWorktimeSeconds() {
        return worktimeSeconds;
    }

    public void setWorktimeSeconds(BigDecimal worktimeSeconds) {
        this.worktimeSeconds = worktimeSeconds;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Worktime_Cobot_REF", joinColumns = {
        @JoinColumn(name = "cobot_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "worktime_id", nullable = false, insertable = false, updatable = false)})
    public Set<Worktime> getWorktimes() {
        return worktimes;
    }

    public void setWorktimes(Set<Worktime> worktimes) {
        this.worktimes = worktimes;
    }
}
