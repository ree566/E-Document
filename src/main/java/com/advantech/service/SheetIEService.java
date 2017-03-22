/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SheetIEService {

    @Autowired
    private SheetIEDAO sheetIEDAO;

    public Collection findAll() {
        return sheetIEDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return sheetIEDAO.findByPrimaryKey(obj_id);
    }

    public String[] getColumnName() {
        return sheetIEDAO.getColumnName();
    }

    public int insert(Object obj) {
        return sheetIEDAO.insert(obj);
    }

    public int update(Object obj) {
        return sheetIEDAO.update(obj);
    }

    public int delete(Object pojo) {
        return sheetIEDAO.delete(pojo);
    }

}
