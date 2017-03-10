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
public class SheetIEService {

    private final SheetIEDAO sheetIEDAO;

    public SheetIEService() {
        sheetIEDAO = new SheetIEDAO();
    }

    public List<Map> getAll() {
        return sheetIEDAO.getAll();
    }

    public List<Map> getColumn() {
        return sheetIEDAO.getColumn();
    }

}
