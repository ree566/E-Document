/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.security.UserProfileType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@Entity
@Table(
        name = "User_Profile",
        schema = "dbo",
        catalog = "E_Document",
        uniqueConstraints = @UniqueConstraint(columnNames = "type")
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserProfile implements java.io.Serializable {

    private int id;
    private String type = UserProfileType.USER.getUserProfileType();
    private Set<User> users = new HashSet<>(0);

    public UserProfile() {
    }

    public UserProfile(int id) {
        this.id = id;
    }

    public UserProfile(int id, String type, Set<User> users) {
        this.id = id;
        this.type = type;
        this.users = users;
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

    @Column(name = "type", length = 50, unique = true, nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "User_Profile_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "user_profile_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", nullable = false, updatable = false)})
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
