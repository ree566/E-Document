/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

/**
 *
 * @author Wei.Cheng
 */
//@Entity
//@Table(
//        name = "HRC",
//        uniqueConstraints = @UniqueConstraint(columnNames = "[name]")
//)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HrcType implements java.io.Serializable {

    private int id;
    private String name;
    
    @JsonIgnore
    private Set<Worktime> worktimes = new HashSet<>(0);

    public HrcType() {
    }

    public HrcType(int id) {
        this.id = id;
    }

    public HrcType(int id, String name, Set<Worktime> worktimes) {
        this.id = id;
        this.name = name;
        this.worktimes = worktimes;
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

    @Column(name = "[name]", length = 100, unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Worktime_HRC_REF", joinColumns = {
        @JoinColumn(name = "hrc_id", nullable = false, insertable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "worktime_id", nullable = false, insertable = false, updatable = false)})
    public Set<Worktime> getWorktimes() {
        return this.worktimes;
    }

    public void setWorktimes(Set<Worktime> worktimes) {
        this.worktimes = worktimes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HrcType)) {
            return false;
        }
        HrcType other = (HrcType) obj;
        if (id != other.id) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserProfile [id=" + id + ",  name=" + name + "]";
    }
}
