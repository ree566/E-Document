/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.SheetIe;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetIEDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    private final Class pojo;

    public SheetIEDAO() {
        pojo = SheetIe.class;
    }

    @Override
    public Collection findAll() {
        return super.findAll("from SheetIe");
    }

    public Collection findAll(PageInfo info) {
        return super.findByCriteria(pojo, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(pojo, integerToLong((Integer) obj_id));
    }

    public String[] getColumnName() {
        return super.getColumnName(pojo);
    }

}
