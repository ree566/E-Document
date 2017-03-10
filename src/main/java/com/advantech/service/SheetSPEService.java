/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class SheetSPEService {

    private final SheetSPEDAO sheetSPEDAO;

    public SheetSPEService() {
        sheetSPEDAO = new SheetSPEDAO();
    }

    public List<Map> getAll() {
        return sheetSPEDAO.getAll();
    }

    public List<Map> getColumn() {
        return sheetSPEDAO.getColumn();
    }

}
