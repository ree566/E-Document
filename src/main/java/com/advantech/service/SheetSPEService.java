/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public List<String> getColumnName() {
        List<Map> l = sheetSPEDAO.getView();
        List<String> columnNames = new ArrayList();
        if (!l.isEmpty()) {
            Iterator entries = l.get(0).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Object key = thisEntry.getKey();
                columnNames.add((String) key);
            }
        }
        return columnNames;
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
