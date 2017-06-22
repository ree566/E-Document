/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Type;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TypeService {

    @Autowired
    private TypeDAO typeDAO;

    public List<Type> findAll() {
        return typeDAO.findAll();
    }

    public List<Type> findAll(PageInfo info) {
        return typeDAO.findAll(info);
    }

    public Type findByPrimaryKey(Object obj_id) {
        return typeDAO.findByPrimaryKey(obj_id);
    }

    public List<Type> findByPrimaryKeys(Integer... id) {
        return typeDAO.findByPrimaryKeys(id);
    }

    public int insert(Type type) {
        return typeDAO.insert(type);
    }

    public int update(Type type) {
        return typeDAO.update(type);
    }

    public int delete(int id) {
        Type type = this.findByPrimaryKey(id);
        return typeDAO.delete(type);
    }

}
