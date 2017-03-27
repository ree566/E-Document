/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Model;
import com.advantech.model.SheetEe;
import com.advantech.model.SheetIe;
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
public class SheetIEService {

    @Autowired
    private ModelDAO modelDAO;

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

    public int insert(SheetIe ie) {
        return sheetIEDAO.insert(ie);
    }

    public int update(Model model, SheetIe ie) {
        Model sameNameModel = modelDAO.findByName(model.getName());

        if (sameNameModel != null && model.getId() != sameNameModel.getId()) {
            return 0;
        }

        Model existModel = (Model) modelDAO.findByPrimaryKey(model.getId());
        if (!model.getName().equals(existModel.getName())) {
            existModel.setName(model.getName());
            modelDAO.update(existModel);
        }
        Set set = existModel.getSheetIes();
        if (set == null || set.isEmpty()) {
            set = new HashSet();
            ie.setModel(existModel);
            set.add(ie);
            return sheetIEDAO.insert(ie);
        } else {
            SheetIe existIESheet = (SheetIe) set.iterator().next();
            ie.setId(existIESheet.getId());
            ie.setModel(existModel);
            return sheetIEDAO.merge(ie);
        }
    }

    public int delete(SheetIe ie) {
        return sheetIEDAO.delete(ie);
    }

}
