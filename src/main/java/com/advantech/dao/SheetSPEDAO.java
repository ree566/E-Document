/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.SheetSpe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetSPEDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    private final Class pojo;

    public SheetSPEDAO() {
        pojo = SheetSpe.class;
    }

    @Override
    public Collection findAll() {
        return super.findAll("from SheetSpe");
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(pojo, integerToLong((Integer) obj_id));
    }

    public List getView() {
        return super.findBySQL("SELECT TOP 1 * FROM Sheet_SPE_view");
    }
}
