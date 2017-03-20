/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Model;
import com.advantech.helper.PageInfo;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ModelDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);
    private final Class pojo;

    public ModelDAO() {
        this.pojo = Model.class;
    }

    @Override
    public Collection findAll() {
        return super.findAll("from Model");
    }

    public Collection findAll(PageInfo info) {
        return super.findByCriteria(pojo, info);
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(pojo, obj_id);
    }

    public List<Model> findByPrimaryKeys(Integer... id) {
        return super.findByPrimaryKeys(pojo, (Object[]) id);
    }

    public List findByName(String modelName) {
        String[] param = {"name"};
        String[] value = {modelName};
        return super.findByHQL("from Model m where m.name = :name", param, value);
    }

}
