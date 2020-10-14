/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.SheetViewDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import java.util.List;
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

    public List<SheetView> findAll() {
        return sheetViewDAO.findAll();
    }

    public List<SheetView> findAll(PageInfo info) {
        return sheetViewDAO.findAll(info);
    }

    public SheetView findByPrimaryKey(Object obj_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int insert(SheetView sheetView) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int update(SheetView sheetView) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int delete(SheetView sheetView) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
