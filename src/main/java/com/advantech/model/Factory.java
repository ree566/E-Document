/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Floor",
        schema = "dbo",
        catalog = "E_Document"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Factory implements Serializable {

    private int id;
    private String name;

    @JsonIgnore
    private Set<BusinessGroup> businessGroups = new HashSet<BusinessGroup>(0);

    @JsonIgnore
    private Set<Floor> floors = new HashSet<Floor>(0);

    @JsonIgnore
    private Set<Flow> flows = new HashSet<Flow>(0);

    @JsonIgnore
    private Set<PreAssy> preAssys = new HashSet<PreAssy>(0);

    @JsonIgnore
    private Set<Type> types = new HashSet<Type>(0);

    @JsonIgnore
    private Set<User> users = new HashSet<User>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BusinessGroup_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "businessGroup_id", nullable = false, updatable = false)})
    public Set<BusinessGroup> getBusinessGroups() {
        return businessGroups;
    }

    public void setBusinessGroups(Set<BusinessGroup> businessGroups) {
        this.businessGroups = businessGroups;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Floor_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "floor_id", nullable = false, updatable = false)})
    public Set<Floor> getFloors() {
        return floors;
    }

    public void setFloors(Set<Floor> floors) {
        this.floors = floors;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Flow_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "flow_id", nullable = false, updatable = false)})
    public Set<Flow> getFlows() {
        return flows;
    }

    public void setFlows(Set<Flow> flows) {
        this.flows = flows;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PreAssy_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "preAssy_id", nullable = false, updatable = false)})
    public Set<PreAssy> getPreAssys() {
        return preAssys;
    }

    public void setPreAssys(Set<PreAssy> preAssys) {
        this.preAssys = preAssys;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Type_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "type_id", nullable = false, updatable = false)})
    public Set<Type> getTypes() {
        return types;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "User_Factory_REF", schema = "dbo", catalog = "E_Document", joinColumns = {
        @JoinColumn(name = "factory_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", nullable = false, updatable = false)})
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
