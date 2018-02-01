/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.view;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 * View object in db
 */
@Entity
@Table(name = "Bab")
public class ModelResponsor implements Serializable {

    private String Model_name;
    private String sitefloor;
    private String SPE_Owner;
    private String EE_Owner;
    private String PQE_Owner;

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String Model_name) {
        this.Model_name = Model_name;
    }

    public String getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(String sitefloor) {
        this.sitefloor = sitefloor;
    }

    public String getSPE_Owner() {
        return SPE_Owner;
    }

    public void setSPE_Owner(String SPE_Owner) {
        this.SPE_Owner = SPE_Owner;
    }

    public String getEE_Owner() {
        return EE_Owner;
    }

    public void setEE_Owner(String EE_Owner) {
        this.EE_Owner = EE_Owner;
    }

    public String getPQE_Owner() {
        return PQE_Owner;
    }

    public void setPQE_Owner(String PQE_Owner) {
        this.PQE_Owner = PQE_Owner;
    }

}
