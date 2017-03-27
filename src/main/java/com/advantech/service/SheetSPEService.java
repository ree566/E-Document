/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Model;
import com.advantech.model.SheetSpe;
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
public class SheetSPEService {

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private SheetSPEDAO sheetSPEDAO;

    public Collection findAll() {
        return sheetSPEDAO.findAll();
    }

    public Object findByPrimaryKey(Object obj_id) {
        SheetSpe spe = (SheetSpe) sheetSPEDAO.findByPrimaryKey(obj_id);

        return spe;
    }

    public String[] getColumnName() {
        return sheetSPEDAO.getColumnName();
    }

    public int insert(SheetSpe spe) {
        return sheetSPEDAO.insert(spe);
    }

    public int update(Model model, SheetSpe spe) {
        Model sameNameModel = modelDAO.findByName(model.getName());

        if (sameNameModel != null && model.getId() != sameNameModel.getId()) {
            return 0;
        }

        Model existModel = (Model) modelDAO.findByPrimaryKey(model.getId());
        if (!model.getName().equals(existModel.getName())) {
            existModel.setName(model.getName());
            modelDAO.update(existModel);
        }
        Set set = existModel.getSheetSpes();
        if (set == null || set.isEmpty()) {
            set = new HashSet();
            spe.setModel(existModel);
            set.add(spe);
            return sheetSPEDAO.insert(spe);
        } else {
            SheetSpe existSPESheet = (SheetSpe) set.iterator().next();
            spe.setId(existSPESheet.getId());
            spe.setModel(existModel);
            return sheetSPEDAO.merge(spe);
        }
    }

    public int delete(String modelName, SheetSpe spe) {
        return sheetSPEDAO.delete(spe);
    }

}
