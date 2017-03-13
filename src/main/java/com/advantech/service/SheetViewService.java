/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SheetViewDAO;
import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewService implements BasicService {

    private final SheetViewDAO sheetViewDAO;

    public SheetViewService() {
        this.sheetViewDAO = new SheetViewDAO();
    }

    @Override
    public Collection findAll() {
        return sheetViewDAO.findAll();
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return sheetViewDAO.findByPrimaryKey(obj_id);
    }

    @Override
    public int insert(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Object pojo) {
        throw new UnsupportedOperationException();
    }

}
