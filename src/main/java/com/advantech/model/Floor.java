package com.advantech.model;
// Generated 2017/4/7 下午 02:26:06 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 * Floor generated by hbm2java
 */
@Entity
@Table(name = "Floor",
         schema = "dbo",
         catalog = "E_Document"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Floor implements java.io.Serializable { 

    private int id;
    private String name;
    private Set<Worktime> worktimes = new HashSet<>(0);
    private Set<User> users = new HashSet<>(0);

    public Floor() {
    }

    public Floor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Floor(int id, String name, Set<Worktime> worktimes, Set<User> users) {
        this.id = id;
        this.name = name;
        this.worktimes = worktimes;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @JsonIgnoreProperties
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "floor")
    public Set<Worktime> getWorktimes() {
        return this.worktimes;
    }

    public void setWorktimes(Set<Worktime> worktimes) {
        this.worktimes = worktimes;
    }

    @JsonIgnore
    @JsonIgnoreProperties
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "floor")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
