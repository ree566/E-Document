/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.BasicDAO;
import com.advantech.model.SheetViewDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewService {

    private final SheetViewDAO sheetViewDAO;

    public SheetViewService() {
        sheetViewDAO = new SheetViewDAO();
    }

    public List<Map> getAll() {
        return sheetViewDAO.getAll();
    }
   
}
