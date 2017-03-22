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
import java.util.Set;
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
    private ModelService modelService;

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

    public String insert(Model m) {
        Set set = m.getSheetSpes();
        SheetSpe spe = (SheetSpe) set.iterator();
        System.out.println(spe.getModifiedDate());
        return "";
    }

    public String insert(String modelName, SheetSpe spe) {

        Model model = modelService.findByName(modelName);
        if (model != null) { //new model
            modelService.insert(model);
            Model insertModel = modelService.findByName(model.getName()); // get the inserted model
            spe.setModel(insertModel);
            sheetSPEDAO.insert(spe);
            return "";
        } else { //model exist
            Set set = model.getSheetSpes();
            if (set != null && !set.isEmpty()) {
                SheetSpe existSpeSheet = (SheetSpe) set.iterator();
                spe.setId(existSpeSheet.getId());
                sheetSPEDAO.merge(spe);
                return "SUCCESS";
            }else{
                spe.setModel(model);
                sheetSPEDAO.insert(spe);
                return "SUCCESS";
            }
        }
    }

    public String update(String modelName, SheetSpe spe) {
        sheetSPEDAO.update(spe);
        return "";
    }

    public String delete(String modelName, SheetSpe spe) {
        sheetSPEDAO.delete(spe);
        return "";
    }

}
