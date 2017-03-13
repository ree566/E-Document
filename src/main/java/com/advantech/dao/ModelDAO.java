/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.entity.Model;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ModelDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);

    public ModelDAO() {

    }

    @Override
    public Collection findAll() {
        return super.findAll("from Model");
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(Model.class, integerToLong((int) obj_id));
    }

}
