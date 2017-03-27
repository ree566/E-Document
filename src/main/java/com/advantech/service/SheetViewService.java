/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SheetViewDAO;
import com.advantech.helper.PageInfo;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SheetViewService {

    @Autowired
    private SheetViewDAO sheetViewDAO;

    public Collection findAll() {
        return sheetViewDAO.findAll();
    }

    public Collection findAll(PageInfo info) {
        return sheetViewDAO.findAll(info);
    }

    public Object findByPrimaryKey(Object obj_id) {
        return sheetViewDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Object obj) {
        return sheetViewDAO.insert(obj);
    }

    public int update(Object obj) {
        return sheetViewDAO.update(obj);
    }

    public int delete(Object pojo) {
        return sheetViewDAO.delete(pojo);
    }

}
