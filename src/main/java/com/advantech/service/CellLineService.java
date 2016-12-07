/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.CellLine;
import com.advantech.model.CellLineDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CellLineService {

    private final CellLineDAO cellDAO;

    private final int LINE_OPEN_SIGN = 1;
    private final int LINE_CLOSE_SIGN = 0;

    protected CellLineService() {
        cellDAO = new CellLineDAO();
    }

    public List<CellLine> findAll() {
        return cellDAO.findAll();
    }

    public CellLine findOne(int id) {
        return cellDAO.findOne(id);
    }

    public boolean login(int id) {
        return cellDAO.updateStatus(id, LINE_OPEN_SIGN);
    }

    public boolean logout(int id) {
        return cellDAO.updateStatus(id, LINE_CLOSE_SIGN);
    }

}
