/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.listener.AuditingRevisionListener;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@RevisionEntity(AuditingRevisionListener.class)
@Table(name = "REVINFO")
public class AuditedRevisionEntity implements Serializable {

    @Id
    @GeneratedValue
    @RevisionNumber
    private int REV;

    @RevisionTimestamp
    private long REVTSTMP;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getREV() {
        return REV;
    }

    public void setREV(int REV) {
        this.REV = REV;
    }

    public long getREVTSTMP() {
        return REVTSTMP;
    }

    public void setREVTSTMP(long REVTSTMP) {
        this.REVTSTMP = REVTSTMP;
    }

}
