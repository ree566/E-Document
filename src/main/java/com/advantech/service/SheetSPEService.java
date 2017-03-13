/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class SheetSPEService implements BasicService {

    private final SheetSPEDAO sheetSPEDAO;

    public SheetSPEService() {
        sheetSPEDAO = new SheetSPEDAO();
    }

    @Override
    public Collection findAll() {
        return sheetSPEDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return sheetSPEDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        return sheetSPEDAO.insert(obj);
    }

    @Override
    public int update(Object obj) {
        return sheetSPEDAO.update(obj);
    }

    @Override
    public int delete(Object pojo) {
        return sheetSPEDAO.delete(pojo);
    }

}
