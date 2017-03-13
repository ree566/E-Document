/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.entity.Identit;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(IdentitDAO.class);

    public IdentitDAO() {

    }

    @Override
    public Collection findAll() {
        return super.findAll("from Identit");
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(Identit.class, integerToLong((int) obj_id));
    }

    public Identit findByJobnumber(String jobnumber) {
        String HQL = "from Identit i where i.jobnumber = :jobnumber";
        Object[] params = super.fillParamToArray("jobnumber");
        Object[] values = super.fillParamToArray(jobnumber);
        List<Identit> l = super.findByHQL(HQL, params, values);
        return l.isEmpty() ? null : l.get(0);
    }

}
