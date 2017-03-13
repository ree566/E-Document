/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    public SheetViewDAO() {

    }

    @Override
    public Collection findAll() {
        return super.findBySQL("SELECT * FROM Sheet_Main_view");
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
