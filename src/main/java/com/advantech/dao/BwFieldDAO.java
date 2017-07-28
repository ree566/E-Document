/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BwField;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BwFieldDAO extends AbstractDao<Integer, BwField> {

    public int update() {
        Session session = this.getSession();
        ProcedureCall call = session.createStoredProcedureCall("sp_update_bwField");
        call.getOutputs();
        return 1;
    }

}
