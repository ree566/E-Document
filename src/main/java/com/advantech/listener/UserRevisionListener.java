/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.model.Identit;
import org.hibernate.envers.RevisionListener;
import org.slf4j.MDC;

/**
 *
 * @author Wei.Cheng
 */
public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Identit exampleRevEntity = (Identit) revisionEntity;
        exampleRevEntity.setUsername(MDC.get("username"));
    }
}
