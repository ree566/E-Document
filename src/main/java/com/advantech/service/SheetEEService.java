/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Model;
import com.advantech.model.SheetEe;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SheetEEService {

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private SheetEEDAO sheetEEDAO;

    public Collection findAll() {
        return sheetEEDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        return sheetEEDAO.findByPrimaryKey(obj_id);
    }

    public String[] getColumnName() {
        return sheetEEDAO.getColumnName();
    }

    public int insert(SheetEe ee) {
        return sheetEEDAO.insert(ee);
    }

    public int update(Model model, SheetEe ee) {
        Model sameNameModel = modelDAO.findByName(model.getName());

        if (sameNameModel != null && model.getId() != sameNameModel.getId()) {
            return 0;
        }

        Model existModel = (Model) modelDAO.findByPrimaryKey(model.getId());
        if (!model.getName().equals(existModel.getName())) {
            existModel.setName(model.getName());
            modelDAO.update(existModel);
        }
        Set set = existModel.getSheetEes();
        if (set == null || set.isEmpty()) {
            set = new HashSet();
            ee.setModel(existModel);
            set.add(ee);
            return sheetEEDAO.insert(ee);
        } else {
            SheetEe existEESheet = (SheetEe) set.iterator().next();
            ee.setId(existEESheet.getId());
            ee.setModel(existModel);
            return sheetEEDAO.merge(ee);
        }
    }

    public int delete(SheetEe ee) {
        return sheetEEDAO.delete(ee);
    }

}
