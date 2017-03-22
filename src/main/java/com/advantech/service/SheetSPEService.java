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
public class SheetSPEService {

    @Autowired
    private SheetSPEDAO sheetSPEDAO;

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
