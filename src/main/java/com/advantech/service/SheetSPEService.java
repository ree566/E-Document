/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class SheetSPEService {

    private final SheetSPEDAO sheetSPEDAO;

    public SheetSPEService() {
        sheetSPEDAO = new SheetSPEDAO();
    }

    public Collection findAll() {
        return sheetSPEDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return sheetSPEDAO.findByPrimaryKey(obj_id);
    }

    public String[] getColumnName() {
        return sheetSPEDAO.getColumnName();
    }

    public int insert(Object obj) {
        return sheetSPEDAO.insert(obj);
    }

    public int update(Object obj) {
        return sheetSPEDAO.update(obj);
    }

    public int delete(Object pojo) {
        return sheetSPEDAO.delete(pojo);
    }

}
