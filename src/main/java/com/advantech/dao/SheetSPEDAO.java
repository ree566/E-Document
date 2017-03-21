/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Model;
import com.advantech.model.SheetSpe;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

    public Collection findAll(PageInfo info) {
        return super.findByCriteria(pojo, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(pojo, integerToLong((Integer) obj_id));
    }

    public List findByModel(Model m) {
        String[] params = {"id"};
        Integer[] values = {m.getId()};
        return super.findByHQL("from SheetSpe spe where spe.model.id = :id", params, values);
    }

    public List getView() {
        return Arrays.asList(super.getColumnName(pojo));
    }
}
