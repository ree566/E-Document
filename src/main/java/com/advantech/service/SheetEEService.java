/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class SheetEEService {

    private final SheetEEDAO sheetEEDAO;

    public SheetEEService() {
        sheetEEDAO = new SheetEEDAO();
    }

    public Collection findAll() {
        return sheetEEDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return sheetEEDAO.findByPrimaryKey(obj_id);
    }

    public String[] getColumnName() {
        return sheetEEDAO.getColumnName();
    }

    public int insert(Object obj) {
        return sheetEEDAO.insert(obj);
    }

    public int update(Object obj) {
        return sheetEEDAO.update(obj);
    }

    public int delete(Object pojo) {
        return sheetEEDAO.delete(pojo);
    }

}
